//
//  drexelclass.m
//  Drexel GPA Calc
//
//  Created by Tomer Shemesh on 4/27/14.
//  Copyright (c) 2014 Tomer Shemesh. All rights reserved.
//

#import "drexelclass.h"

@implementation drexelclass
{
    NSString *_name;
    float _credits;
    NSString * _grade;
}

- (id)initWithClass:(NSString *)name credits:(float)credits grade:(NSString *)grade
{
    self = [super init];
    if (self)
    {
        _name = name;
        _credits = credits;
        _grade = grade;
    }
    
    return self;
}


- (NSString *)getName
{
    return _name;
}

- (float)getCredits
{
    return _credits;
}

- (NSString *)getGrade
{
    return _grade;
}


-(void)setName:(NSString *)name
{
    _name = name;
}

-(void)setCredits:(float)credits
{
    _credits = credits;
}

-(void)setGrade:(NSString *)grade
{
    _grade = grade;
}





@end
