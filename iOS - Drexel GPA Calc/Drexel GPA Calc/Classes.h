//
//  Classes.h
//  Drexel GPA Calc
//
//  Created by Tomer on 5/14/14.
//  Copyright (c) 2014 Tomer Shemesh. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <CoreData/CoreData.h>


@interface Classes : NSManagedObject

@property (nonatomic, retain) NSNumber * classCreditsValue;
@property (nonatomic, retain) NSString * classGradeValue;
@property (nonatomic, retain) NSString * classNameValue;
@property (nonatomic, retain) NSNumber * termValue;
@property (nonatomic, retain) NSNumber * yearValue;

@end
