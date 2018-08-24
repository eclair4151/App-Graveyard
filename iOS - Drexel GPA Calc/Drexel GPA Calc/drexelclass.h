//
//  drexelclass.h
//  Drexel GPA Calc
//
//  Created by Tomer Shemesh on 4/27/14.
//  Copyright (c) 2014 Tomer Shemesh. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface drexelclass : NSObject



- (id)initWithClass:(NSString *)name credits:(float)credits grade:(NSString *)grade;

- (NSString *)getName;
- (float)getCredits;
- (NSString *)getGrade;

-(void)setName:(NSString *)name;
-(void)setCredits:(float)credits;
-(void)setGrade:(NSString *)grade;
 


@end
