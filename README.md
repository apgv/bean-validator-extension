# bean-validator-extension

Password validator with custom validation message hiding the actual password value from the error message.

The payload parameter `NoLogging.class` is intended to be used by a framework to exclude the value failing validation
from being logged – that is the value returned from `javax.validation.ConstraintViolation.getInvalidValue()`.

## Usage

- Parameter `min` is optional. Defaults to 10. Absolute minimum password length is 4. 
- Parameter `max` is optional. Defaults to 128.
- Parameter `payload` is required and is expected to be `NoLogging.class` or else an exception is thrown.

```java
import javax.validation.Payload;

interface NoLogging extends Payload {
}
```

```java
import codes.foobar.validator.NoLogging;

import javax.validation.Payload;

public class Foo {

    @Password(min = 6, max = 12, payload = NoLogging.class)
    private String password;

    public Foo(String password) {
        this.password = password;
    }
}
```
## Example validation error messages

#### Invalid length password
Password length must be between 6 and 12 but was: 3

#### Empty password
Password length must be between 6 and 12 but was: 0