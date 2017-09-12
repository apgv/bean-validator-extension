# bean-validator-extension

Password validator with custom validation message hiding the actual password value.

## Usage

Parameter `minLength` is optional. Defaults to 10.

```java
public class Foo {

    @Password(minLength = 4)
    private String password;

    public Foo(String password) {
        this.password = password;
    }
}
```
## Example validation error messages

#### Too short password
Password length must be greater than or equal to 4 but was: 3

#### Password is null
Password length must be greater than or equal to 4 but was: null