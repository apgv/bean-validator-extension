# bean-validator-extension

Password validator with custom validation message hiding the actual password value.

## Usage

Parameter `min` is optional. Defaults to 10.
Parameter `max` is optional. Defaults to 128.

```java
public class Foo {

    @Password(min = 6, max = 12)
    private String password;

    public Foo(String password) {
        this.password = password;
    }
}
```
## Example validation error messages

#### Invalid length password
Password length must be between 6 and 12 but was: 3

#### Password is null
Password length must be between 6 and 12 but was: null