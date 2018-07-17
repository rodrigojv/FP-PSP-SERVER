package py.org.fundacionparaguaya.pspserver.families.utils;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static py.org.fundacionparaguaya.pspserver.util.TestMockFactory.MOCK_PERSON;

/**
 * Created by rodrigovillalba on 7/13/18.
 */
public class FamilyHelperTest {


    @Test
    public void shouldGenerateCode() {
        String code = FamilyHelper.generateFamilyCode(MOCK_PERSON);
        assertThat(code).isNotNull();
        System.out.println(code);
    }


}
