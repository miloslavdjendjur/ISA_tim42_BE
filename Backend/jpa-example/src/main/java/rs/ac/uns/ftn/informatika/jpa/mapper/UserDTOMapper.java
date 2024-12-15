package rs.ac.uns.ftn.informatika.jpa.mapper;


import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import rs.ac.uns.ftn.informatika.jpa.dto.ShowUserDTO;
import rs.ac.uns.ftn.informatika.jpa.model.User;

import java.util.stream.Collectors;

@Component
public class UserDTOMapper {

    private static ModelMapper modelMapper;

    @Autowired
    public UserDTOMapper(ModelMapper modelMapper) {
        UserDTOMapper.modelMapper = modelMapper;
    }

    public static ShowUserDTO fromUserToDTO(User user, long postCount, long followersCount) {
        ShowUserDTO dto = new ShowUserDTO(
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                postCount,
                user.getNumberOfPeopleFollowing(),
                user.getUsername(),
                (int) followersCount, // Cast long to int
                null // Followers list is no longer directly included
        );
        return dto;
    }


}
