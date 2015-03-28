//
//  MonochromeImageReader.m
//  monochromeImageDemo
//
//  Created by 倪 李俊 on 15/3/28.
//  Copyright (c) 2015年 com.sinri. All rights reserved.
//

#import "MonochromeImageReader.h"
#import "SSZipArchive.h"


@implementation MonochromeImageReader

+(UIImage*)getImage:(NSString *)zipPath{
    NSString * cachedMNCPath=[CommonUtil CachePath:[zipPath.lastPathComponent stringByDeletingPathExtension]];
    _Log(@"zipPath=%@ and MNC in Cache Path=%@",zipPath,cachedMNCPath);
    if([[NSFileManager defaultManager]fileExistsAtPath:cachedMNCPath]){
        //UIImage * image= [UIImage imageWithContentsOfFile:cachedMNCPath];
        NSData* data=[NSData dataWithContentsOfFile:cachedMNCPath];
        UIImage * image=  [MonochromeImageReader UIImageDataFromMNCImage:data];
        _Log(@"read cache image as %@",image);
        return image;
    }else{
        NSString * cached=[MonochromeImageReader unzipToCachedMNC:zipPath];
        if(cached){
            NSData* data=[NSData dataWithContentsOfFile:cached];
            UIImage * image=  [MonochromeImageReader UIImageDataFromMNCImage:data];
            _Log(@"read built cache image as %@",image);
            return image;
        }else{
            _Log(@"failed built cache image");
            return nil;
        }
    }
}

+(NSString*)unzipToCachedMNC:(NSString*)zipPath{
    NSString * cachedMNCPath=[CommonUtil CachePath:[zipPath.lastPathComponent stringByDeletingPathExtension]];
    
    BOOL done=[SSZipArchive unzipFileAtPath:zipPath toDestination:[CommonUtil CachePath]];
    if(done){
        return cachedMNCPath;
    }else{
        return nil;
    }
}

+(UIColor*)colorWithGrayScaleByte:(int)grayScale{
    return [UIColor colorWithWhite:grayScale/255.0 alpha:1];
}

+(UIImage *)UIImageDataFromMNCImage:(NSData *)MNCData{
    if(MNCData && MNCData.length>0){
        Byte * buffer=(Byte*)[MNCData bytes];
        
        int width=0;
        int height=0;
        //        [MNCData getBytes:&width range:NSMakeRange(0, 4)];
        //        [MNCData getBytes:&height range:NSMakeRange(4, 4)];
        width=(buffer[0]<<24)+(buffer[1]<<16)+(buffer[2]<<8)+(buffer[3]);
        height=(buffer[4]<<24)+(buffer[5]<<16)+(buffer[6]<<8)+(buffer[7]);
        
        Byte grayBits=1;
        [MNCData getBytes:&grayBits range:NSMakeRange(8, 1)];
        
        NSLog(@"width=%@ height=%@,grayBits=%@",@(width),@(height),@(grayBits));
        if(width*height<=0){
            return nil;
        }
        
        //通过自己创建一个context来绘制，通常用于对图片的处理
        /*
         解释一下UIGraphicsBeginImageContextWithOptions函数参数的含义：第一个参数表示所要创建的图片的尺寸；第二个参数用来指定所生成图片的背景是否为不透明，如上我们使用YES而不是NO，则我们得到的图片背景将会是黑色，显然这不是我想要的；第三个参数指定生成图片的缩放因子，这个缩放因子与UIImage的scale属性所指的含义是一致的。传入0则表示让图片的缩放因子根据屏幕的分辨率而变化，所以我们得到的图片不管是在单分辨率还是视网膜屏上看起来都会很好。
         */
        //该函数会自动创建一个context，并把它push到上下文栈顶，坐标系也经处理和UIKit的坐标系相同
        UIGraphicsBeginImageContextWithOptions(CGSizeMake(width, height), NO, 0);
        CGContextRef context = UIGraphicsGetCurrentContext();
        
        NSUInteger pixel_id=0;
        for (NSUInteger i=9; i<MNCData.length; i++) {
            Byte unit=buffer[i];
            
            //Get Gray Mask
            int grayMask=unit & 0x000000FF;
            grayMask=(grayMask>>(8-grayBits))<<(8-grayBits);
            for(int j=0;j<8;j++){
                grayMask=grayMask | (grayMask>>grayBits);
            }
            UIColor * color=[MonochromeImageReader colorWithGrayScaleByte:grayMask];
            [color setFill];
            //Get Gray Length
            int length=unit & 0x000000FF;
            length=((length<<grayBits)&0x000000FF)>>grayBits;
            
            for (int j=0; j<length; j++) {
                //write color to current pixel_id
                unsigned long x=pixel_id % width;
                unsigned long y=pixel_id / width;
                
                CGContextFillRect(context, CGRectMake(x, y, 1, 1));
                //pixel inc
                pixel_id++;
            }
        }
        //把当前context的内容输出成一个UIImage图片
        UIImage* img = UIGraphicsGetImageFromCurrentImageContext();
        //上下文栈pop出创建的context
        UIGraphicsEndImageContext();
        
        return img;
        
    }else{
        return nil;
    }
}

@end
