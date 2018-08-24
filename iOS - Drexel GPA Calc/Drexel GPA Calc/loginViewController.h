//
//  loginViewController.h
//  Drexel GPA Calc
//
//  Created by Tomer Shemesh on 7/24/14.
//  Copyright (c) 2014 Tomer Shemesh. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "drexelclass.h"
#import "ViewController.h"

@interface loginViewController : UIViewController <UITextFieldDelegate>
@property (weak, nonatomic) IBOutlet UIButton *closeButton;
@property (weak, nonatomic) IBOutlet UIButton *getClassesButton;
@property (weak, nonatomic) IBOutlet UITextField *usernameBox;
@property (weak, nonatomic) IBOutlet UITextField *passwordBox;
@property (weak, nonatomic) IBOutlet UIActivityIndicatorView *progressSpinner;
@property (weak, nonatomic) IBOutlet UITextView *resultBox;
- (void)giveInfoAtStart:(ViewController *)classtable;
@end
