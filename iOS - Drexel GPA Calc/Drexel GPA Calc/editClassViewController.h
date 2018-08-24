//
//  editClassViewController.h
//  Drexel GPA Calc
//
//  Created by Tomer Shemesh on 5/8/14.
//  Copyright (c) 2014 Tomer Shemesh. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "drexelclass.h"
#import "ViewController.h"
@interface editClassViewController : UIViewController
@property (weak, nonatomic) IBOutlet UIButton *editClassCloseButton;
@property (weak, nonatomic) IBOutlet UIButton *editClassGradeButton;
@property (weak, nonatomic) IBOutlet UITextField *editClassNameField;
@property (weak, nonatomic) IBOutlet UITextField *editClassCreditField;
@property (weak, nonatomic) IBOutlet UIButton *editClassSaveButton;
@property (weak, nonatomic) IBOutlet UIPickerView *editClassPickerView;
@property (weak, nonatomic) IBOutlet UIToolbar *editClassToolBar;
@property (weak, nonatomic) IBOutlet UIBarButtonItem *editClassToolBarDoneButton;
@property (strong, nonatomic) IBOutlet UITapGestureRecognizer *editClassTapToDismiss;

- (void)giveInfoAtStart:(drexelclass*)classToEdit tableview:(ViewController *)classtable;
- (int)gradeToIndex:(NSString *)grade;
@end
