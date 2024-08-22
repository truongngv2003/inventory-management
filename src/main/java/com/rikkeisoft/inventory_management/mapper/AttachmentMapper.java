package com.rikkeisoft.inventory_management.mapper;

import com.rikkeisoft.inventory_management.dto.AttachmentDTO;
import com.rikkeisoft.inventory_management.model.Attachment;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface AttachmentMapper {

    AttachmentMapper INSTANCE = Mappers.getMapper(AttachmentMapper.class);

    Attachment toAttachment(AttachmentDTO attachmentDTO);

    AttachmentDTO toAttachmentDTO(Attachment attachment);
}
