package pl.matros.resource;


import io.smallrye.mutiny.Multi;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pl.matros.dto.ErrorResponseDto;
import pl.matros.dto.RepositoryDto;
import pl.matros.service.GitHubService;

@Path("/api/v1/repositories")
@Produces(MediaType.APPLICATION_JSON)
public class GitHubResource {

    private final GitHubService gitHubService;

    public GitHubResource(GitHubService gitHubService) {
        this.gitHubService = gitHubService;
    }

    @GET
    @Path("/{username}")
    public Multi<RepositoryDto> getRepositories(@PathParam("username") String username) {
        return gitHubService.getRepositories(username);
    }

    @jakarta.ws.rs.ext.Provider
    public static class UserNotFoundExceptionMapper implements jakarta.ws.rs.ext.ExceptionMapper<GitHubService.UserNotFoundException> {
        @Override
        public Response toResponse(GitHubService.UserNotFoundException exception) {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .entity(new ErrorResponseDto(404, exception.getMessage()))
                    .build();
        }
    }
}