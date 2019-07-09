package com.github.leeonky.util;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PropertyWriterTest {
    private BeanClass<BeanWithPubField> beanWithPubFieldBeanClass = new BeanClass<>(BeanWithPubField.class);

    @Test
    void set_field_value() {
        BeanWithPubField bean = new BeanWithPubField();
        beanWithPubFieldBeanClass.setPropertyValue("field", bean, 100);
        assertThat(bean.field).isEqualTo(100);
    }

    @Test
    void set_value_via_setter_override_field() {
        BeanWithPubField bean = new BeanWithPubField();
        beanWithPubFieldBeanClass.setPropertyValue("field2", bean, 100);
        assertThat(bean.field2).isEqualTo(200);
    }

    @Test
    void should_raise_error_when_no_reader() {
        assertThrows(IllegalArgumentException.class, () ->
                beanWithPubFieldBeanClass.setPropertyValue("notExist", new BeanWithPubField(), null));
    }

    public static class BeanWithPubField {
        public int field;
        public int field2;
        private int privateField;

        public void setField2(int i) {
            field2 = i + 100;
        }
    }
}
