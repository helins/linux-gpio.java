# linux-gpio

[
![Download](https://api.bintray.com/packages/dvlopt/maven/linux-gpio/images/download.svg)
](https://bintray.com/dvlopt/maven/linux-gpio/_latestVersion)

[![Maven
Central](https://maven-badges.herokuapp.com/maven-central/io.dvlopt/linux-gpio/badge.svg)](https://maven-badges.herokuapp.com/maven-central/io.dvlopt/linux-gpio)


Handle [GPIO](https://en.wikipedia.org/wiki/General-purpose_input/output) lines 
on a Linux system in a portable and fast way from the JVM.

- Control several lines at once
- Surprisingly fast
- Better support for interrupts than others usual methods
- Standard

## Rationale

Handling GPIO lines from user space on a Linux system is typically done by
writing directly to /dev/mem or to the more permissive /dev/gpiomem. While being
fast, this approach is very closely thight to the hardware which means the code
is not portable.  Another way is to use the sysfs method where each pin is
exported and represented in the file system. Users can read and write specific
files for configuration and IO. While being standard, this method is slow and
deprecated.  Both methods present caveats. For instance, there is no way to
claim pins nor any kind of automatic clean-up.

Since Linux 4.8, a new API is provided. It is not widely known nor used in spite
of the fact it is standard and fast. Certain platforms such as Raspbian on the
Raspberry Pi already support this API. This Java library wraps the C API in
order to provide a more idiomatic interface accessible from the JVM while trying
not to be too opiniated.

## Usage

Any accessed GPIO device need at least read permission (which is enough even
for writing). Typically, only the root user has access, hence permissions need
to be set. For instance, for device 0 on a Raspberry Pi (user pi) :

```bash
$ chown root:pi /dev/gpiochip0
$ chmod g+r /dev/gpiochip0
```

Here is the [javadoc](https://dvlopt.github.io/doc/java/linux-gpio/index.html).

Have a look at the [examples](./examples).

Run an example where `$EXAMPLE` is the name of an example such as
`AlternatingLeds` :
```bash
$ ./gradlew :examples:$EXAMPLE:run
```

Run tests :
```bash
$ ./gradlew test
```

Run tests on the Raspberry Pi 3 (or similar), will test some IO :
```bash
$ ./gradlew test -DonRaspberry=true
```

## Resources

- [Linux
API](https://github.com/torvalds/linux/blob/master/include/uapi/linux/gpio.h)

- [New GPIO interface for user
space](https://www.youtube.com/watch?v=cdTLewJCL1Y&t=2s)
[Slides](https://www.elinux.org/images/7/74/Elce2017_new_GPIO_interface.pdf), a
talk presenting the new Linux API as well as a more user-friendly C library
built on top of it.

- [GPIO for engineers and makers](https://www.youtube.com/watch?v=lQRCDl0tFiQ)
[Slides](https://elinux.org/images/9/9b/GPIO_for_Engineers_and_Makers.pdf), a
talk presenting what happens on the Kernel side as well as the user side with
the new API.

## License

Licensed under the [Apache License, Version
2.0](http://www.apache.org/licenses/LICENSE-2.0).

Copyright © 2018 Adam Helinski
