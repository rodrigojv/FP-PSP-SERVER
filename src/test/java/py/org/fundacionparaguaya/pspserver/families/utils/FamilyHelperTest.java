package py.org.fundacionparaguaya.pspserver.families.utils;

import org.junit.Test;
import py.org.fundacionparaguaya.pspserver.util.TestMockFactory;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by rodrigovillalba on 7/13/18.
 */
public class FamilyHelperTest {


    @Test
    public void shouldGenerateCode() {
        String code = FamilyHelper.generateFamilyCode(TestMockFactory.aPerson());
        assertThat(code).isNotNull();
        System.out.println(code);
    }


}
