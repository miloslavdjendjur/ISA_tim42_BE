package rs.ac.uns.ftn.informatika.jpa.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import rs.ac.uns.ftn.informatika.jpa.model.Image;
import rs.ac.uns.ftn.informatika.jpa.repository.ImageRepository;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.imgscalr.Scalr;

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
        System.out.println("Saving image to path: " + path);

        File folder = new File(uploadDir);
        if (uploadDir == null || uploadDir.isEmpty()) {
            throw new IllegalStateException("Upload directory is not set.");
        }
        if (!folder.exists()) {
            folder.mkdirs();
        }

        try (FileOutputStream fos = new FileOutputStream(new File(folder, fileName))) {
            fos.write(file.getBytes());
        }

        Image image = new Image();
        image.setPath(path);
        image.setUploadDate(LocalDate.now());
        image.setCompressed(false);
        Image savedImage = imageRepository.save(image);
        System.out.println("Image saved with path: " + savedImage.getPath());
        return savedImage;
    }

    public void compressImagesOlderThanOneMonth() {
        LocalDate oneMonthAgo = LocalDate.now().minusMonths(1);
        List<Image> oldImages = imageRepository.findImagesOlderThan(oneMonthAgo);

        for (Image image : oldImages) {
            try {
                String absolutePath = new File("src/main/resources/static" + image.getPath()).getAbsolutePath();
                File originalFile = new File(absolutePath);
                BufferedImage originalImage = ImageIO.read(originalFile);

                if (originalImage == null) {
                    System.err.println("Failed to read image: " + absolutePath);
                    continue;
                }

                BufferedImage compressedImage = Scalr.resize(originalImage, Scalr.Method.QUALITY, 800, 600);

                String compressedPath = absolutePath.replace(".png", "_compressed.png");
                File compressedFile = new File(compressedPath);
                ImageIO.write(compressedImage, "png", compressedFile);
                System.out.println("Compressed image saved to: " + compressedPath);

            } catch (IOException e) {
                System.err.println("Error compressing image: " + image.getPath());
                e.printStackTrace();
            }
        }
    }

}

