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

    public static ShowUserDTO fromUserToDTO(User user,long postCount) {

        ShowUserDTO dto = new ShowUserDTO(
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                postCount,
                user.getNumberOfPeopleFollowing(),
                user.getUsername(),
                user.getFollowersCount(),
                user.getFollowers().stream()
                        .map(User::getId)
                        .collect(Collectors.toList())
        );
        return dto;
    }
    /*public static User fromDTOToUser(ShowUserDTO dto) {
        User user = modelMapper.map(dto, User.class);
        return user;
    }*/

}
