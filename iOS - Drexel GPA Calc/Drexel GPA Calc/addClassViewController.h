//
//  addClassViewController.h
//  Drexel GPA Calc
//
//  Created by Tomer on 4/30/14.
//  Copyright (c) 2014 Tomer Shemesh. All rights reserved.
//

#import <UIKit/UIKit.h>

@protocol addClassDelegate,UIAlertView;

@interface addClassViewController : UIViewController
@property (weak, nonatomic) IBOutlet UIButton *closeButton;
@property (weak, nonatomic) IBOutlet UISearchBar *searchClassTextField;
@property (weak, nonatomic) IBOutlet UIPickerView *gradePickerView;
@property (nonatomic, strong) NSArray *gradeArray;
@property (weak, nonatomic) IBOutlet UIButton *editGradeButton;
@property (weak, nonatomic) IBOutlet UIButton *addClassButton;
@property (weak, nonatomic) IBOutlet UIToolbar *doneGradeToolBar;
@property (weak, nonatomic) IBOutlet UIBarButtonItem *doneGradeButton;
@property (weak, nonatomic) IBOutlet UITableView *allClassTable;
@property (strong, nonatomic) IBOutlet UITapGestureRecognizer *tapToDismiss;
@property (nonatomic, weak) id<addClassDelegate> delegate;
@end

@protocol addClassDelegate <NSObject>

- (void)addClassViewController:(addClassViewController *)viewController addClass:(NSString *)name credits:(float)credits grade:(NSString *)grade;

@end
