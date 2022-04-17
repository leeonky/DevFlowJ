package com.github.leeonky.util;

import com.github.leeonky.dal.extensions.StringExtension;
import hastype.One;
import hastype.Two;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import subtype.Base;
import subtype.Sub1;
import subtype.Sub2;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

import static com.github.leeonky.util.BeanClass.allTypesIn;
import static com.github.leeonky.util.BeanClass.subTypesOf;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BeanClassTest {

    @Test
    void get_type() {
        assertThat(BeanClass.create(String.class).getType()).isEqualTo(String.class);
    }

    @Test
    void get_name() {
        assertThat(BeanClass.create(String.class).getName()).isEqualTo(String.class.getName());
    }

    @Test
    void get_simple_name() {
        assertThat(BeanClass.create(String.class).getSimpleName()).isEqualTo(String.class.getSimpleName());
    }

    @Test
    void new_instance() {
        assertThat(BeanClass.create(String.class).newInstance()).isEqualTo("");
    }

    @Test
    void new_instance_with_arg() {
        assertThat(BeanClass.create(String.class).newInstance("hello")).isEqualTo("hello");
    }

    @Test
    void new_instance_failed_when_no_candidate_constructor() {
        assertThrows(NoAppropriateConstructorException.class,
                () -> BeanClass.create(BeanClassTest.class).newInstance("hello"));
    }

    @Test
    void get_class_name() {
        assertThat(BeanClass.getClassName("")).isEqualTo(String.class.getName());

        assertThat(BeanClass.getClassName(null)).isEqualTo(null);
    }

    @Test
    void create_default_value() {
        assertThat(BeanClass.create(int.class).createDefault()).isEqualTo(0);
        assertThat(BeanClass.create(Integer.class).createDefault()).isNull();
    }

    @Test
    void get_generic_params() {
        assertThat(BeanClass.create(Integer.class).hasTypeArguments()).isFalse();
        assertThat(BeanClass.create(Integer.class).getTypeArguments(0)).isEmpty();
    }

    @Test
    void hash_code() {
        assertThat(BeanClass.create(Integer.class).hashCode())
                .isEqualTo(Objects.hash(BeanClass.class, Integer.class));
    }

    @Test
    void bean_class_equal() {
        assertThat(new BeanClass<>(Integer.class)).isEqualTo(new BeanClass<>(Integer.class));
    }

    @Test
    void get_class_from_instance() {
        assertThat(BeanClass.getClass(new BeanClassTest())).isEqualTo(BeanClassTest.class);
    }

    @Test
    void create_from_instance() {
        assertThat(BeanClass.createFrom(new BeanClassTest())).isEqualTo(BeanClass.create(BeanClassTest.class));
    }

    static class StringList extends ArrayList<String> {
    }

    static class SubStringList extends StringList {
    }

    static class StringSupplier implements Supplier<String> {
        @Override
        public String get() {
            return null;
        }
    }

    static class SubStringSupplier extends StringSupplier {
    }

    @Nested
    class GetSuper {

        @Test
        void should_return_bean_class_by_given_class() {
            BeanClass<ArrayList> beanClass = BeanClass.create(StringList.class).getSuper(ArrayList.class);

            assertThat(beanClass.getType()).isEqualTo(ArrayList.class);
            assertThat(beanClass.getTypeArguments(0).get().getType()).isEqualTo(String.class);
        }

        @Test
        void should_return_bean_class_bygiven__grand_farther_class() {
            BeanClass<ArrayList> beanClass = BeanClass.create(SubStringList.class).getSuper(ArrayList.class);

            assertThat(beanClass.getType()).isEqualTo(ArrayList.class);
            assertThat(beanClass.getTypeArguments(0).get().getType()).isEqualTo(String.class);
        }

        @Test
        void should_return_bean_class_by_given_interface() {
            BeanClass<Supplier> beanClass = BeanClass.create(StringSupplier.class).getSuper(Supplier.class);

            assertThat(beanClass.getType()).isEqualTo(Supplier.class);
            assertThat(beanClass.getTypeArguments(0).get().getType()).isEqualTo(String.class);
        }

        @Test
        void should_return_bean_class_by_given_grand_farther_interface() {
            BeanClass<Supplier> beanClass = BeanClass.create(SubStringSupplier.class).getSuper(Supplier.class);

            assertThat(beanClass.getType()).isEqualTo(Supplier.class);
            assertThat(beanClass.getTypeArguments(0).get().getType()).isEqualTo(String.class);
        }
    }

    @Nested
    class GetTypesInPackage {

        @Test
        void empty_when_no_type() {
            assertThat(allTypesIn("empty")).isEmpty();
        }

        @Test
        void types_in_package() {
            assertThat(new HashSet<>(allTypesIn("hastype")))
                    .containsOnly(One.class, Two.class, Two.Three.class, Two.Four.class);
        }

        @Test
        void sub_types_in_package() {
            assertThat(new HashSet<>(subTypesOf(Base.class, "subtype"))).containsOnly(Sub1.class, Sub2.class);
        }

        @Test
        void sub_types_in_package_include_super() {
            assertThat(new HashSet<>(BeanClass.assignableTypesOf(Base.class, "subtype")))
                    .containsOnly(Base.class, Sub1.class, Sub2.class);
        }
    }

    @Nested
    class AllClassesInJar {

        @Test
        void empty_when_no_type() {
            List<Class<?>> classes = allTypesIn("com.github.leeonky.dal.extensions");
            assertThat(classes).contains(StringExtension.class);
        }
    }
}