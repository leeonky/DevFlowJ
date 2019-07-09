package com.github.leeonky.util;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PropertyReaderTest {
    BeanClass<BeanWithPubField> beanWithPubFieldBeanClass = new BeanClass<>(BeanWithPubField.class);

    @Test
    void get_field_value() {
        assertThat(beanWithPubFieldBeanClass.getPropertyValue("field", new BeanWithPubField())).isEqualTo(100);
    }

    @Test
    void get_value_via_getter_override_field() {
        assertThat(beanWithPubFieldBeanClass.getPropertyValue("field2", new BeanWithPubField())).isEqualTo(200);
    }

    @Test
    void should_support_boolean_getter() {
        assertTrue((Boolean) beanWithPubFieldBeanClass.getPropertyValue("bool", new BeanWithPubField()));
    }

    @Test
    void should_raise_error_when_no_reader() {
        assertThrows(IllegalArgumentException.class, () ->
                beanWithPubFieldBeanClass.getPropertyValue("boolean", new BeanWithPubField()));

        assertThrows(IllegalArgumentException.class, () ->
                beanWithPubFieldBeanClass.getPropertyValue("privateField", new BeanWithPubField()));
    }

    public static class BeanWithPubField {
        public final int field = 100;
        public final int field2 = 0;
        private final int privateField = 1;

        public int getField2() {
            return 200;
        }

        public boolean isBool() {
            return true;
        }

        public Boolean isBoolean() {
            return true;
        }
    }
}
