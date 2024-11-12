package rs.ac.uns.ftn.informatika.jpa.model;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import rs.ac.uns.ftn.informatika.jpa.service.ImageService;

@Component
@EnableScheduling
public class ImageCompressionScheduler {

    private final ImageService imageService;

    public ImageCompressionScheduler(ImageService imageService) {
        this.imageService = imageService;
    }

    // Pokreće se svaki dan u ponoć
    @Scheduled(cron = "0 0 0 * * ?")
    public void compressOldImages() {
        imageService.compressImagesOlderThanOneMonth();
    }
}
