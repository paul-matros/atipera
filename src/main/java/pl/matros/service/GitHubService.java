package pl.matros.service;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.WebApplicationException;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import pl.matros.client.GitHubApiClient;
import pl.matros.dto.BranchDto;
import pl.matros.dto.RepositoryDto;
import pl.matros.model.GitHubBranch;
import pl.matros.model.GitHubRepository;

import java.util.List;

@ApplicationScoped
public class GitHubService {

    private final GitHubApiClient gitHubApiClient;

    public GitHubService(@RestClient GitHubApiClient gitHubApiClient) {
        this.gitHubApiClient = gitHubApiClient;
    }

    public Multi<RepositoryDto> getRepositories(String username) {
        return gitHubApiClient.getRepositories(username)
                .onItem().transformToMulti(repositories -> Multi.createFrom().iterable(repositories))
                .onFailure().transform(throwable -> handleRepositoryFailure(throwable, username))
                .filter(repository -> !repository.isFork())
                .onItem().transformToUni(this::getBranchesForRepository)
                .merge();
    }

    private Uni<RepositoryDto> getBranchesForRepository(GitHubRepository repository) {
        return gitHubApiClient.getBranches(repository.getOwner().getLogin(), repository.getName())
                .map(branches -> mapToRepositoryResponseDto(repository, branches));
    }

    private RepositoryDto mapToRepositoryResponseDto(GitHubRepository repository, List<GitHubBranch> branches) {
        List<BranchDto> branchDtos = branches.stream()
                .map(branch -> new BranchDto(branch.getName(), branch.getCommit().getSha()))
                .toList();

        return new RepositoryDto(repository.getName(), repository.getOwner().getLogin(), branchDtos);
    }

    private Throwable handleRepositoryFailure(Throwable failure, String username) {
        if (failure instanceof WebApplicationException exception
                && exception.getResponse().getStatus() == 404) {
            return new UserNotFoundException(String.format("User '%s' not found", username));
        }
        return failure;
    }

    public static class UserNotFoundException extends RuntimeException {
        public UserNotFoundException(String message) {
            super(message);
        }
    }
}