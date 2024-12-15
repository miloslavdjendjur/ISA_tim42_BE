package rs.ac.uns.ftn.informatika.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rs.ac.uns.ftn.informatika.jpa.model.Notification;
import rs.ac.uns.ftn.informatika.jpa.model.User;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByRecipientOrderByCreatedTimeDesc(User recipient);
    List<Notification> findByRecipientAndReadFalseOrderByCreatedTimeDesc(User recipient);
}
