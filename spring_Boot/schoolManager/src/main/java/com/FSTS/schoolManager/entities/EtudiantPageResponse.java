package com.FSTS.schoolManager.entities;

import com.FSTS.schoolManager.dtos.EtudiantDto;

import java.util.List;
public record EtudiantPageResponse(List<EtudiantDto> etudiant,
                                   Integer pageNumber,
                                   Integer pageSize,
                                   long totalElements,
                                   int totalPages,
                                   boolean isLast) {

}
