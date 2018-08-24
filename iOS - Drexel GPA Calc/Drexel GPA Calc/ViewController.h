//
//  ViewController.h
//  Drexel GPA Calc
//
//  Created by Tomer Shemesh on 4/24/14.
//  Copyright (c) 2014 Tomer Shemesh. All rights reserved.
//


#import <UIKit/UIKit.h>
#import "addClassViewController.h"
#import "PodFiles/SWTableViewCell.h"

@interface ViewController : UIViewController <UIPickerViewDelegate,UIPickerViewDataSource,addClassDelegate,SWTableViewCellDelegate,UIAlertViewDelegate>{
    
    
}
@property (weak, nonatomic) IBOutlet UIPickerView *pickerview;
@property (nonatomic, strong) NSArray *yearArray;
@property (nonatomic, strong) NSArray *termArray;
@property (weak, nonatomic) IBOutlet UIButton *yearButton;
@property (weak, nonatomic) IBOutlet UIButton *termButton;
@property (weak, nonatomic) IBOutlet UIBarButtonItem *doneButton;
@property (weak, nonatomic) IBOutlet UIToolbar *doneToolBar;
@property (weak, nonatomic) IBOutlet UITableView *classTable;
@property (weak, nonatomic) IBOutlet UITextField *creditsThisTerm;
@property (weak, nonatomic) IBOutlet UITextField *creditsOverall;
@property (weak, nonatomic) IBOutlet UITextField *GPAThisTerm;
@property (weak, nonatomic) IBOutlet UITextField *GPAOverall;
@property (weak, nonatomic) IBOutlet UIButton *addClassButton;
@property (weak, nonatomic) IBOutlet UIButton *GPAForcasterButton;
@property (weak, nonatomic) IBOutlet UIImageView *loginButton;
@property (weak,nonatomic) IBOutlet NSLayoutConstraint *doneToolBarTopContraint;
@property (weak,nonatomic) IBOutlet NSLayoutConstraint *pickerViewTopContraint;

- (void)reloadhome:(BOOL) reloaddata;
- (void)addClass:(NSString*)name grade:(NSString*)grade credits:(float)credits year:(int)year term:(int)term;
- (void)removeAllClasses;
- (void)saveClasses;
@end
