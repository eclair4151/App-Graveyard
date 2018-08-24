//
//  gpaForcasterViewController.h
//  Drexel GPA Calc
//
//  Created by Tomer Shemesh on 5/8/14.
//  Copyright (c) 2014 Tomer Shemesh. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface gpaForcasterViewController : UIViewController
@property (strong, nonatomic) IBOutlet UITapGestureRecognizer *gpaForecastTpToDismiss;
@property (weak, nonatomic) IBOutlet UITextField *gpaForcastTotalCredits;
@property (weak, nonatomic) IBOutlet UITextField *gpaForecastCurrentGpa;
@property (weak, nonatomic) IBOutlet UITextField *gpaForecastDesiredGpa;
@property (weak, nonatomic) IBOutlet UIButton *gpaForecastCalculateButton;
@property (weak, nonatomic) IBOutlet UIButton *gpaForecastCloseButton;
@property (weak, nonatomic) IBOutlet UITextView *gpaForecastResultLabel;


- (void)giveInfoAtStart:(float)totalCredits currentGPA:(float)currentGPA;

@end
