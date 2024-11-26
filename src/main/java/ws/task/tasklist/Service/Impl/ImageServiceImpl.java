package ws.task.tasklist.Service.Impl;

import io.minio.BucketExistsArgs;
import io.minio.MinioClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ws.task.tasklist.Entity.TaskImage;
import ws.task.tasklist.Exception.ImageUploadException;
import ws.task.tasklist.Service.ImageService;
import ws.task.tasklist.Service.props.MinioProperties;

import java.io.InputStream;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    private final MinioClient minioClient;
    private final MinioProperties minioProperties;


    @Override
    public String upload(TaskImage image) {
        try {
            createBucket();
        } catch (Exception e) {
            throw new ImageUploadException("Image upload failed" + e.getMessage());
        }
        MultipartFile file = image.getFile();
        if (file.isEmpty() || file.getOriginalFilename() == null) {
            throw new ImageUploadException("Image must have name");
        }
        String fileName = generateFileName(file);
        InputStream inputStream;
        try {
            inputStream = file.getInputStream();
        } catch (Exception e) {
            throw new ImageUploadException("Image upload failed" + e.getMessage());
        }
        saveImage(inputStream, fileName);
        return fileName;
    }

    private void createBucket() {
        boolean found = minioClient.bucketExists(BucketExistsArgs.builder()
                .build());
    }

}
