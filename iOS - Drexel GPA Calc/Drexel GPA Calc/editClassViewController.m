//
//  editClassViewController.m
//  Drexel GPA Calc
//
//  Created by Tomer Shemesh on 5/8/14.
//  Copyright (c) 2014 Tomer Shemesh. All rights reserved.
//

#import "editClassViewController.h"
#import "drexelclass.h"
@interface editClassViewController ()

@end

@implementation editClassViewController
@synthesize editClassCreditField,editClassGradeButton,editClassNameField,editClassSaveButton,editClassPickerView,editClassToolBar,editClassToolBarDoneButton,editClassTapToDismiss;
ViewController *classTable;
drexelclass *myclass;
NSArray *gradeArray;
float animationtime2 = 0.3;
- (void)viewDidLoad
{
    [super viewDidLoad];
    editClassNameField.text = [myclass getName];
    editClassCreditField.text = [ NSString stringWithFormat:@"%g",[myclass getCredits]];
    editClassTapToDismiss.cancelsTouchesInView = NO;
    //editClassGradeButton.titleLabel.text = [myclass getGrade];
    [editClassGradeButton setTitle:[myclass getGrade] forState:UIControlStateNormal];
    gradeArray = [[NSArray alloc] initWithObjects:@"A+",@"A",@"A-",@"B+",@"B",@"B-",@"C+",@"C",@"C-",@"D+",@"D",@"D-",@"F",@"CR",@"NC", nil];
    [editClassPickerView setUserInteractionEnabled:YES];
    int pickerval = [self gradeToIndex:[myclass getGrade]];
    [editClassPickerView selectRow:pickerval inComponent:0 animated:NO];
    [editClassCreditField setKeyboardType:UIKeyboardTypeDecimalPad];
    // Do any additional setup after loading the view.
}

- (IBAction)editClassTapToDismissTapped:(id)sender {
    [self dismissKeyboard];
    
}

-(BOOL) textFieldShouldReturn:(UITextField *)textField{
    
    [self dismissKeyboard];
    return YES;
}

-(void)dismissKeyboard
{
    [editClassNameField resignFirstResponder];
    [editClassCreditField resignFirstResponder];
}

- (void)viewDidAppear:(BOOL)animated
{
    [super viewDidAppear:animated];
    
    [editClassPickerView setFrame:(CGRectMake(0,(self.view.frame.size.height),editClassPickerView.frame.size.width,editClassPickerView.frame.size.height))];
    [editClassToolBar setFrame:CGRectMake(0,self.view.frame.size.height,editClassToolBar.frame.size.width,editClassToolBar.frame.size.height)];
    
}



- (IBAction)editClassCloseButtonPress:(id)sender
{
    [self dismissViewControllerAnimated:YES completion:nil];
}

- (IBAction)editClassToolBarDoneButtonPress:(id)sender
{
    int gradespot = (int)[editClassPickerView selectedRowInComponent:0];
//    editClassGradeButton.titleLabel.text =gradeArray[gradespot];
    [editClassGradeButton setTitle:gradeArray[gradespot] forState:UIControlStateNormal];
    
    [editClassPickerView setUserInteractionEnabled:NO];
    [UIView animateWithDuration:animationtime2 animations:^{
        [editClassPickerView setFrame:(CGRectMake(0,(self.view.frame.size.height),editClassPickerView.frame.size.width,editClassPickerView.frame.size.height))];
        [editClassToolBar setFrame:CGRectMake(0,self.view.frame.size.height,editClassToolBar.frame.size.width,editClassToolBar.frame.size.height)];
    }];
    
}
- (IBAction)editClassGradeButtonPress:(id)sender
{
       [UIView animateWithDuration:animationtime2 animations:^{
        
        [editClassPickerView setFrame:(CGRectMake(0,(self.view.frame.size.height-editClassPickerView.frame.size.height),editClassPickerView.frame.size.width,editClassPickerView.frame.size.height))];
        [editClassToolBar setFrame:CGRectMake(0,editClassPickerView.frame.origin.y,editClassToolBar.frame.size.width,editClassToolBar.frame.size.height)];
        
    }];
    [editClassPickerView setUserInteractionEnabled:YES];

}

- (IBAction)editClassSaveButtonPressed:(id)sender
{
    [myclass setName:editClassNameField.text];
    [myclass setGrade:editClassGradeButton.titleLabel.text];
    [myclass setCredits:[editClassCreditField.text floatValue]];
    [classTable reloadhome:true];
    [classTable saveClasses];
    [self dismissViewControllerAnimated:YES completion:nil];
    
}

- (void)giveInfoAtStart:(drexelclass*)classToEdit tableview:(ViewController *)classtable
{
    myclass = classToEdit;
    classTable = classtable;
}



- (NSInteger)numberOfComponentsInPickerView:(UIPickerView *)pickerView
{
    return 1;  // returns the number of 'columns' to display.
}

- (NSInteger)pickerView:(UIPickerView *)pickerView numberOfRowsInComponent:(NSInteger)component
{
    return [gradeArray count];
}

- (NSString *)pickerView:(UIPickerView *)pickerView titleForRow:(NSInteger)row forComponent:(NSInteger)component
{
    return [gradeArray objectAtIndex:row];
}


- (int)gradeToIndex:(NSString *)grade
{
    int index = 0;
    if([grade isEqualToString:@"A"])
    {
        index = 1;
    }
    else if([grade isEqualToString:@"A-"])
    {
        index = 2;
    }
    else if([grade isEqualToString:@"B+"])
    {
        index = 3;
    }
    else if([grade isEqualToString:@"B"])
    {
        index = 4;
    }
    else if([grade isEqualToString:@"B-"])
    {
        index = 5;
    }
    else if([grade isEqualToString:@"C+"])
    {
        index = 6;
    }
    else if([grade isEqualToString:@"C"])
    {
        index = 7;
    }
    else if([grade isEqualToString:@"C-"])
    {
        index = 8;
    }
    else if([grade isEqualToString:@"D+"])
    {
        index = 9;
    }
    else if([grade isEqualToString:@"D"])
    {
        index = 10;
    }
    else if([grade isEqualToString:@"D-"])
    {
        index = 11;
    }
    else if([grade isEqualToString:@"F"])
    {
        index = 12;
    }
    else if([grade isEqualToString:@"CR"])
    {
        index = 13;
    }
    else if([grade isEqualToString:@"NC"])
    {
        index = 14;
    }
    
    return index;
}


@end
