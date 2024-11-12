package rs.ac.uns.ftn.informatika.jpa.service;
import org.hibernate.cfg.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import rs.ac.uns.ftn.informatika.jpa.model.Image;
import rs.ac.uns.ftn.informatika.jpa.repository.ImageRepository;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;
import java.util.logging.Logger;

@Service
public class ImageService {

    private final ImageRepository imageRepository;

    @Autowired
    public ImageService(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    @Value("${app.image.upload-dir:${user.home}/myapp/images/posts}")
    private String uploadDir;

    public Image saveImage(MultipartFile file) throws IOException {
        String fileName = UUID.randomUUID().toString() + ".png";
        String path = "/images/posts/" + fileName;
        System.out.println("Saving image to path: " + path); // Debugging line

        File folder = new File(uploadDir);
        if (uploadDir == null || uploadDir.isEmpty()) {
            throw new IllegalStateException("Upload directory is not set.");
        }
        if (!folder.exists()) {
            folder.mkdirs();
        }

        // Write file to disk
        try (FileOutputStream fos = new FileOutputStream(new File(folder, fileName))) {
            fos.write(file.getBytes());
        }

        // Save image metadata to database
        Image image = new Image();
        image.setPath(path);
        Image savedImage = imageRepository.save(image); // Save Image entity
        System.out.println("Image saved with path: " + savedImage.getPath()); // Debugging line
        return savedImage; // Return the entire Image entity
    }
}

