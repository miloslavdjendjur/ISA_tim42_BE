package rs.ac.uns.ftn.informatika.jpa.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import rs.ac.uns.ftn.informatika.jpa.dto.CommentDTO;
import rs.ac.uns.ftn.informatika.jpa.model.Comment;

@Component
public class CommentDTOMapper {

    private static ModelMapper modelMapper;

    @Autowired
    public CommentDTOMapper(ModelMapper modelMapper) {
        CommentDTOMapper.modelMapper = modelMapper;
    }

    public static Comment fromDTOtoComment(CommentDTO dto) {
        return modelMapper.map(dto, Comment.class);
    }

    public static CommentDTO fromCommentToDTO(Comment comment) {
        return modelMapper.map(comment, CommentDTO.class);
    }
}
