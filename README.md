# linux-gpio

Handle [GPIO](https://en.wikipedia.org/wiki/General-purpose_input/output) pins
on a Linux system in a portable and fast way.

## Rationale

Handling GPIO pins on a Linux system is typically done by writing directly to
/dev/mem or the more permissive /dev/gpiomem. While being fast, this approach is
very closely thight to the hardware which means the code is not portable.
Another way is to use the sysfs method where each pin is exported and
represented in the file system.  Users can read and write specific files for
configuration and IO. While being standard, this method is slow and deprecated.
Both methods present caveats. For instance, there is no way to claim pins nor
any kind of automatic clean-up.

Since Linux 4.8, a new API is provided. It is not widely known nor used in spite
of the fact it is standard and surprisingly fast. Certain platforms such as
Raspbian on the Raspberry Pi already supports this API. This Java library wraps
the C API in order to provide a lighter and more idiomatic interface accessible
from the JVM while trying on the be too opiniated.

## Resources

- [Linux
API](https://github.com/torvalds/linux/blob/master/include/uapi/linux/gpio.h)

- [New GPIO interface for user
space](https://www.youtube.com/watch?v=cdTLewJCL1Y&t=2s)
[Slides](https://www.elinux.org/images/7/74/Elce2017_new_GPIO_interface.pdf), a
talk presenting the new Linux API as well as a more user-friendly C library
build on top of it.

- [GPIO for engineers and makers](https://www.youtube.com/watch?v=lQRCDl0tFiQ)
[Slides](https://elinux.org/images/9/9b/GPIO_for_Engineers_and_Makers.pdf), a
talk presenting what happens on the Kernel side as well as the user side with
the new API.

## License

MIT License

Copyright © 2018 Adam Helinski

Permission is hereby granted, free of charge, to any person obtaining a copy of
this software and associated documentation files (the “Software”), to deal in
the Software without restriction, including without limitation the rights to
use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
the Software, and to permit persons to whom the Software is furnished to do so,
subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
