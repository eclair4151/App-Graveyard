//
//  termClasses.m
//  Drexel GPA Calc
//
//  Created by Tomer Shemesh on 4/27/14.
//  Copyright (c) 2014 Tomer Shemesh. All rights reserved.
//

#import "termClasses.h"
#import "drexelclass.h"

@implementation termClasses
{
    NSMutableArray *allClasses;
}

- (id)init
{
    self = [super init];
    if (self) {
        
        allClasses = [NSMutableArray arrayWithCapacity:0];
    }
    return self;
}


- (void)addClass:(NSString *)name credits:(float)credits grade:(NSString *)grade
{
    drexelclass *currClass = [[drexelclass alloc] initWithClass:name credits:credits grade:grade];
    [allClasses addObject:currClass];
}

- (void)removeClass:(int)index
{
    [allClasses removeObjectAtIndex:index];
}

- (NSString *)getClass:(int)index
{
    NSString *one = [allClasses[index] getName];
    NSString *two = [NSString stringWithFormat:@"%.1f", [allClasses[index] getCredits]];
    NSString *three = [allClasses[index] getGrade];
    
    NSString *responce = [NSString stringWithFormat:@"%@        %@        %@", one, two, three];
    return responce;
}

- (drexelclass *)getClassObject:(int)index
{
    return allClasses[index];
}


- (int)numOfClasses
{
    return (int)[allClasses count];
}



- (float)getTermGpa
{
    float gpa =0.0;
    for (int i=0; i<[allClasses count]; i++)
    {
        gpa += [allClasses[i] getCredits] * [self gradeToGPA:([allClasses[i] getGrade]) ];
    }
    if ([self getTotalTermCredits:false] != 0)
    {
        gpa = gpa/[self getTotalTermCredits:false];
    }
    
    return gpa;
}



- (float)getTotalTermCredits:(BOOL)includecr
{
    float totalgpa =0.0;
    for (int i =0; i<[allClasses count]; i++)
    {
        if( (includecr || ![[allClasses[i] getGrade] isEqual: @"CR"]) && ![[allClasses[i] getGrade] isEqual: @"NC"]&& ![[allClasses[i] getGrade] isEqual: @"NF"] && ![[allClasses[i] getGrade] isEqual: @"DCU"] && ![[allClasses[i] getGrade] isEqual: @"W"])
        {
            totalgpa += [allClasses[i] getCredits];
        }
        
    }
    return totalgpa;
}

- (void)removeClasses
{
    [allClasses removeAllObjects];
}

- (float)gradeToGPA:(NSString *)grade
{
    float gpa = 0;
    if([grade isEqualToString:@"A+"] ||[grade isEqualToString:@"A"])
    {
        gpa = 4.0;
    }
    else if([grade isEqualToString:@"A-"])
    {
        gpa = 3.67;
    }
    else if([grade isEqualToString:@"B+"])
    {
        gpa = 3.33;
    }
    else if([grade isEqualToString:@"B"])
    {
        gpa = 3.0;
    }
    else if([grade isEqualToString:@"B-"])
    {
        gpa = 2.67;
    }
    else if([grade isEqualToString:@"C+"])
    {
        gpa = 2.33;
    }
    else if([grade isEqualToString:@"C"])
    {
        gpa = 2.0;
    }
    else if([grade isEqualToString:@"C-"])
    {
        gpa = 1.67;
    }
    else if([grade isEqualToString:@"D+"])
    {
        gpa = 1.33;
    }
    else if([grade isEqualToString:@"D"] || [grade isEqualToString:@"D-"])
    {
        gpa = 1.0;
    }
    return gpa;
}

@end
