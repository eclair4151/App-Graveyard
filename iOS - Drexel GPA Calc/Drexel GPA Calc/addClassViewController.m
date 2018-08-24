//
//  addClassViewController.m
//  Drexel GPA Calc
//
//  Created by Tomer on 4/30/14.
//  Copyright (c) 2014 Tomer Shemesh. All rights reserved.
//

#import "addClassViewController.h"

@interface addClassViewController ()
@end
@implementation addClassViewController

@synthesize searchClassTextField,gradePickerView,gradeArray, editGradeButton, doneGradeButton, doneGradeToolBar,tapToDismiss,allClassTable;
float animationtime1 = .3;
NSArray *allClasses;
NSMutableArray *classesInQuery;
bool selected = false;

- (void)viewDidLoad
{
    [super viewDidLoad];
    gradeArray = [[NSArray alloc] initWithObjects:@"A+",@"A",@"A-",@"B+",@"B",@"B-",@"C+",@"C",@"C-",@"D+",@"D",@"D-",@"F",@"CR",@"NC", nil];
    tapToDismiss.cancelsTouchesInView = NO;
    [gradePickerView setUserInteractionEnabled:YES];
    NSString* path = [[NSBundle mainBundle] pathForResource:@"FinalList" ofType:@"txt"];
    NSString* content = [NSString stringWithContentsOfFile:path encoding:NSUTF8StringEncoding error:NULL];
    allClasses = [content componentsSeparatedByString:@"\n"];
    classesInQuery = [NSMutableArray array];
}

- (void)viewDidAppear:(BOOL)animated
{
    [super viewDidAppear:animated];
    
    [gradePickerView setFrame:(CGRectMake(0,(self.view.frame.size.height),gradePickerView.frame.size.width,gradePickerView.frame.size.height))];
    [doneGradeToolBar setFrame:CGRectMake(0,self.view.frame.size.height,doneGradeToolBar.frame.size.width,doneGradeToolBar.frame.size.height)];
}


- (void)searchBar:(UISearchBar *)searchBar textDidChange:(NSString *)searchText
{
    selected = false;
    [classesInQuery removeAllObjects];
    for (int i =0; i<[allClasses count]; i++)
    {
        if ([[allClasses[i] lowercaseString] rangeOfString:[searchClassTextField.text lowercaseString]].location!=NSNotFound)
        {
            [classesInQuery addObject:allClasses[i]];
        }
    }
    [allClassTable reloadData];
}

-(void)dismissKeyboard
{
    [searchClassTextField resignFirstResponder];
}


- (IBAction)tapToDismissTapped:(id)sender
{
    [self dismissKeyboard];
}



- (IBAction)doneGradeButtonPress:(id)sender
{
    [gradePickerView setUserInteractionEnabled:NO];
    int gradespot = (int)[gradePickerView selectedRowInComponent:0];
    [editGradeButton setTitle:gradeArray[gradespot] forState:UIControlStateNormal];
    
    [UIView animateWithDuration:animationtime1 animations:^{
        [gradePickerView setFrame:(CGRectMake(0,(self.view.frame.size.height),gradePickerView.frame.size.width,gradePickerView.frame.size.height))];
        [doneGradeToolBar setFrame:CGRectMake(0,self.view.frame.size.height,doneGradeToolBar.frame.size.width,doneGradeToolBar.frame.size.height)];
    }];
}


- (IBAction)closeButtonPressed:(id)sender {
    [self dismissViewControllerAnimated:true completion:nil];
}



- (IBAction)addClassButtonPress:(id)sender
{
    if (selected)
    {
        id<addClassDelegate> strongDelegate = self.delegate;
        NSArray *data = [searchClassTextField.text componentsSeparatedByString:@","];
    
        [strongDelegate addClassViewController:self addClass:data[0] credits:[data[2] floatValue] grade:editGradeButton.titleLabel.text];
    }
    else if(![searchClassTextField.text isEqualToString:@""])
    {
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Class Not Found" message:@"We didn't find that class in our system. Enter the number of credits it is worth and we will add it anyway:" delegate:self cancelButtonTitle:@"Done" otherButtonTitles:@"Cancel",nil];
        alert.alertViewStyle = UIAlertViewStylePlainTextInput;
        [[alert textFieldAtIndex:0] setKeyboardType:UIKeyboardTypeDecimalPad];
        [[alert textFieldAtIndex:0] becomeFirstResponder];
        [alert setTag:0];
        [alert show];
    }
}

-(void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex
{
    if (buttonIndex == 0 && [alertView tag] == 0)
    {
        id<addClassDelegate> strongDelegate = self.delegate;
        float credits =[[alertView textFieldAtIndex:0].text floatValue];
        if (credits!=0)
        {
            [strongDelegate addClassViewController:self addClass:searchClassTextField.text credits:credits grade:editGradeButton.titleLabel.text];
        }
        else
        {
            UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Error" message:@"Not a valid number" delegate:self cancelButtonTitle:@"OK" otherButtonTitles:nil];
            alert.alertViewStyle = UIAlertViewStyleDefault;
            [alert setTag:1];
            [alert show];
        }
        
    }
}

- (void)searchBarSearchButtonClicked:(UISearchBar *)searchbar
{
    [self dismissKeyboard];
}

- (IBAction)editGradeButtonPress:(id)sender
{
    [UIView animateWithDuration:animationtime1 animations:^{
        
        [gradePickerView setFrame:(CGRectMake(0,(self.view.frame.size.height-gradePickerView.frame.size.height),gradePickerView.frame.size.width,gradePickerView.frame.size.height))];
        [doneGradeToolBar setFrame:CGRectMake(0,gradePickerView.frame.origin.y,doneGradeToolBar.frame.size.width,doneGradeToolBar.frame.size.height)];
        
    }];
    [gradePickerView setUserInteractionEnabled:YES];
}



- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return [classesInQuery count];
}


- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    NSString *simpleTableIdentifier = @"classTableCell";
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:simpleTableIdentifier];
    
    if (cell==nil)
        cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier: simpleTableIdentifier];
    cell.textLabel.text = classesInQuery[indexPath.row];
    return cell;
    
}



- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    searchClassTextField.text = classesInQuery[indexPath.row];
    [classesInQuery removeAllObjects];
    [allClassTable reloadData];
    selected = true;
    [tableView deselectRowAtIndexPath:indexPath animated:YES];
}

- (void)scrollViewDidScroll:(UIScrollView *)scrollView
{
    [self dismissKeyboard];
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



@end
