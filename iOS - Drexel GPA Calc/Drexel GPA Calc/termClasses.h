//
//  termClasses.h
//  Drexel GPA Calc
//
//  Created by Tomer Shemesh on 4/27/14.
//  Copyright (c) 2014 Tomer Shemesh. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "drexelclass.h"
@interface termClasses : NSObject
{
    
}
    


- (void)addClass:(NSString *)name credits:(float)credits grade:(NSString *)grade;
- (void)removeClass:(int)index;
- (NSString *)getClass:(int)index;
- (drexelclass *)getClassObject:(int)index;
- (int)numOfClasses;
- (float)getTermGpa;
- (id)init;
- (float)getTotalTermCredits:(BOOL)includecr;
- (float)gradeToGPA:(NSString *)grade;
- (void)removeClasses;
    




@end
