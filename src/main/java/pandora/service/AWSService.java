package pandora.service;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import java.awt.Image;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.imageio.ImageIO;
import pandora.domain.StoredImage;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class AWSService {
    
    private final String BUCKET = "pandora.images";
    
    public StoredImage deposit(StoredImage image, MultipartFile newFile) throws IOException{
        if(image == null || newFile == null) {
            return null;
        }
        String key = null;
        try {
            key = uploadSingleItem(newFile, image.getId().toString());
        } catch(FileNotFoundException fnfExc) {
            throw fnfExc;
        } catch(IOException ioExc){
            throw ioExc;
        }
        if(key == null || key.length() == 0){
            image = null;
        } 
        
        return image;
    }
    
    public void delete(StoredImage image) throws IOException {
        if(image != null && image.getId() > 0){
            AmazonS3 s3Client = getClient();
            try {
                s3Client.deleteObject(new DeleteObjectRequest(BUCKET, image.getId().toString()));
            } catch (AmazonServiceException ase) {
                throw new IllegalArgumentException("AWS-yhteyden avaaminen ei onnistu!");
            } catch (AmazonClientException ace) {
                throw new IllegalArgumentException("AWS-operaatio ei onnistu!");
            }
        }        
    }
    
    public Image retrieve(String storageKey) throws IOException {
        if(storageKey == null || storageKey.length() == 0){
            return null;
        }
        Image img = null;
        try {
            img = returnSingleImage(storageKey);
        } catch (IOException ioExc){
            throw ioExc;
        }
        return img;
    }
    
    private String uploadSingleItem(MultipartFile file, String key) throws FileNotFoundException, IOException{
        AmazonS3 s3Client = getClient();
        try{
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(file.getSize());
            PutObjectRequest putObjectRequest = new PutObjectRequest(BUCKET, key, file.getInputStream(), objectMetadata);
            PutObjectResult putResult = s3Client.putObject(putObjectRequest);
        } catch (AmazonServiceException ase) {
            return null;
        } catch (AmazonClientException ace) {
            return null;
        }
        return key;
    }
    
    private Image returnSingleImage(String key) throws IOException{
        AmazonS3 s3Client = getClient();
        Image img = null;
        try{
            GetObjectRequest request = new GetObjectRequest(BUCKET, key);
            S3Object s3Object = s3Client.getObject(request);
            S3ObjectInputStream objectContent = s3Object.getObjectContent();            
            img = ImageIO.read(objectContent);
        } catch (AmazonServiceException ase) {
            return null;
        } catch (AmazonClientException ace) {
            return null;
        }
        return img;
    }
    
    private AmazonS3 getClient() throws IOException{
        return new AmazonS3Client();
    }
}
