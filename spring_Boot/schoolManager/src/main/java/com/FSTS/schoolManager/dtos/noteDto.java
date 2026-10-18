package com.FSTS.schoolManager.dtos;

import com.FSTS.schoolManager.entities.Note;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class noteDto {

    Integer idNote;

    Double note;

    String type;

    Note.TypeExamen typeExamen;
}
