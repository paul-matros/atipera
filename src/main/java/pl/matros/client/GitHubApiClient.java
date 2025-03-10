package pl.matros.client;


import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import org.eclipse.microprofile.rest.client.annotation.ClientHeaderParam;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import pl.matros.model.GitHubBranch;
import pl.matros.model.GitHubRepository;

import java.util.List;

@RegisterRestClient(configKey = "github-api")
@ClientHeaderParam(name = "Accept", value = "application/vnd.github.v3+json")
public interface GitHubApiClient {

    @GET
    @Path("users/{username}/repos")
    Uni<List<GitHubRepository>> getRepositories(@PathParam("username") String username);

    @GET
    @Path("/repos/{owner}/{repo}/branches")
    Uni<List<GitHubBranch>> getBranches(@PathParam("owner") String owner, @PathParam("repo") String repo);

}