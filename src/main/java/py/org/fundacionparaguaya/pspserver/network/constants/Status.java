package py.org.fundacionparaguaya.pspserver.network.constants;

/**
 *
 * @author mgonzalez
 *
 */
public enum Status {

   ACTIVE("A"), INACTIVE("I");

    private String value;

    Status (String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

}

