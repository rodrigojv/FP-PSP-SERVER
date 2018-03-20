package py.org.fundacionparaguaya.pspserver.common.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by rodrigovillalba on 2/16/18.
 * <p>
 * Simple exception with a message, that returns an Internal Server Error code.
 */
@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class InternalServerErrorException extends RuntimeException {

    public InternalServerErrorException(String msg) {
        super(msg);
    }

    public InternalServerErrorException(Throwable e) {
        super(e);
    }

}
