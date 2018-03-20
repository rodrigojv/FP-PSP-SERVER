package py.org.fundacionparaguaya.pspserver.common.entities;

import java.io.Serializable;

import javax.persistence.Inheritance;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
@Inheritance
public abstract class BaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;

}