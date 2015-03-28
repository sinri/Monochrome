//
//  MonochromeImageReader.h
//  monochromeImageDemo
//
//  Created by 倪 李俊 on 15/3/28.
//  Copyright (c) 2015年 com.sinri. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>


@interface MonochromeImageReader : NSObject

+(UIImage*)getImage:(NSString *)zipPath;

+(NSString*)unzipToCachedMNC:(NSString*)zipPath;
+(UIImage*)UIImageDataFromMNCImage:(NSData*)MNCData;

@end
