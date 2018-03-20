package py.org.fundacionparaguaya.pspserver.common.exceptions;

/**
 * {@code AWSS3RuntimeException} Exception for handling Amazon Web Service S3
 * service exceptions during an image upload to a S3 bucket.
 *
 */
public class AWSS3RuntimeException extends RuntimeException {

    public AWSS3RuntimeException() {
        super();
    }

    public AWSS3RuntimeException(String message) {
        super(message);
    }

    public AWSS3RuntimeException(Throwable cause) {
        super(cause);
    }
}
