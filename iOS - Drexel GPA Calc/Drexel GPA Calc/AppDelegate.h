//
//  AppDelegate.h
//  Drexel GPA Calc
//
//  Created by Tomer Shemesh on 4/24/14.
//  Copyright (c) 2014 Tomer Shemesh. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface AppDelegate : UIResponder <UIApplicationDelegate>

@property (strong, nonatomic) UIWindow *window;
@property (nonatomic, retain, readonly) NSManagedObjectModel *managedObjectModel;
@property (nonatomic, retain, readonly) NSManagedObjectContext *managedObjectContext;
@property (nonatomic, retain, readonly) NSPersistentStoreCoordinator *persistentStoreCoordinator ;

-(void)saveContext;
-(NSURL *)applicationDocumentsDirectory;
@end
