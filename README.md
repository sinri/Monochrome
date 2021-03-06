# Monochrome
Monochromatic Image Compress Solution

## Why Monochrome

One day I had a requirement to make up an app on iOS to display hymns without downloading. After I got the scaned pages of hymnal books, the app was built up quickly. Yes, thanks God.

Then what's the problem? The size of the images was too large. Download an app with more than 200MB form App Store is a nightmare in almost area of China Mainland. It might be the only way to make the images take lower space.

With the mercy of the Lord, the original images are simple with only white and black pixels. Of cause actually there are pixels in other gray scales, in eyes they are almost one unit. To compress them, one side is to decrease the space cost for color, and the other side is to unit as much pixels with same color as one unit to record.

## Algorithm

The algorithm, finally, was designed as following as Byte-Stream of configs and units. It is an specialized implementation of Run-Length-Encoding.

### Byte-Stream

The Monochrome Image record the image as gray-scaled matrix. The first 4 bytes contains the number of pixels in width side, and the next 4 bytes contains the number of pixels in height side. The 9th byte recorded the number of bits to show gray scale. Form the 10th byte on, each byte contains one unit, which contains color and length and could be explained in details beneath. The units are set one by one form the first row on top to the last row on bottom, and from left to right in each row.

### Configs

The first 4 bytes contains the number of pixels in width side, and the next 4 bytes contains the number of pixels in height side. To compute the width and height, commonly use 

	// Java, C, C++, Objective-C
	int width=(buffer[0]<<24)+(buffer[1]<<16)+(buffer[2]<<8)+(buffer[3]);
    int height=(buffer[4]<<24)+(buffer[5]<<16)+(buffer[6]<<8)+(buffer[7]);
    
Width and height should not be larger than 2^31 (or 2^32 if parsed as unsigned).
	
The 9th byte recorded the number of bits to show gray scale.
 
    Byte grayBits = buffer[8];
 	
The gray bits should be a number with one and seven, it decides the number of bits in one byte unit used for gray scale. For example, when it was set to 2, any pixel in the unit would have four choices of color in gray scale, 0x00, 0x55, 0xAA, and 0xFF.

### Unit

One unit in the stream takes one byte. The first X bits contains the gray scale information, and the rest contains the lenth. It is easier to understand with a sample: 0x48 under 2-bit gray scale.

The first 2 bits of 0x48 is 0b01, and the rest (8-2) bits is 0b001000. So that this unit is maked up with 0b00001000 (4) pixels of gray scale 0b01010101 (i.e. 0x55, RGB as #555555).

## Compress

It might be as good as PNG when using 2-bit gray scale, but as it contains much more same encoding, it could be compressed to 50% or so with any Entropy Encoding. For easy, zip is a good choice.

## Implementation and Usage

The complete implementation is given in Java.

For iOS, a read toolkit class provided, which supports to return UIImage instance with NSData input from `.mnc` file or from `.mnc.zip` file. It needs [CommonUtil](https://github.com/sinri/SinriXcodeBasement) and [SSZipArchive](https://github.com/soffes/ssziparchive).



