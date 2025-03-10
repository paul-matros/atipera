package pl.matros.dto;

import java.util.List;

public record RepositoryDto(String name,
                            String ownerLogin,
                            List<BranchDto> branches) {
}