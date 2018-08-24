//
//  ViewController.m
//  Drexel GPA Calc
//
//  Created by Tomer Shemesh on 4/24/14.
//  Copyright (c) 2014 Tomer Shemesh. All rights reserved.
//

#import "ViewController.h"
#import "addClassViewController.h"
#import "termClasses.h"
#import "PodFiles/SWTableViewCell.h"
#import "editClassViewController.h"
#import "drexelclass.h"
#import "gpaForcasterViewController.h"
#import "AppDelegate.h"
#import <CoreData/CoreData.h>
#import "Classes.h"
#import "AFNetworking.h"
#import "HTMLParser.h"
#import "loginViewController.h"
@interface ViewController ()
@end

@implementation ViewController
@synthesize pickerview,yearArray,termArray,doneToolBar,classTable,creditsOverall,creditsThisTerm,GPAForcasterButton,GPAOverall,GPAThisTerm,addClassButton,doneToolBarTopContraint,pickerViewTopContraint;
NSMutableArray *yearAndTermsArray;
int year,term;
float animationtime = .3;
drexelclass *classToEdit;

- (void)viewDidLoad
{
    [super viewDidLoad];
   
    [self setupYearAndTermsArray];
    
}

- (void)viewDidAppear:(BOOL)animated
{
    [super viewDidAppear:animated];
    //[pickerview setFrame:(CGRectMake(0,(self.view.frame.size.height),pickerview.frame.size.width,pickerview.frame.size.height))];
    //[doneToolBar setFrame:CGRectMake(0,self.view.frame.size.height,doneToolBar.frame.size.width,doneToolBar.frame.size.height)];
    
    if (![[NSUserDefaults standardUserDefaults] boolForKey:@"HasLaunchedOnce"])
    {
        
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Sign in to Drexel One?"
                                                        message:@"Would you like to sign into Drexel One to download all you past classes? If not you can always click the icon in the top right corner!"
                                                       delegate:nil
                              
                                              cancelButtonTitle:@"No"
                                              otherButtonTitles:@"Yes",nil];
        [alert setDelegate:self];
        [alert show];
        [[NSUserDefaults standardUserDefaults] setBool:YES forKey:@"HasLaunchedOnce"];
        [[NSUserDefaults standardUserDefaults] synchronize];
        
    }

}

- (void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex
{
    if(buttonIndex == 1)
    {
         [self performSegueWithIdentifier: @"loginSegue" sender: self];
    }
}


-(void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender{
    if([segue.identifier isEqualToString:@"addClassSegue"])
    {
        addClassViewController *addclasscontroller = segue.destinationViewController;
        addclasscontroller.delegate = self;
        [self.navigationController pushViewController:addclasscontroller animated:YES];
    }
    else if([segue.identifier isEqualToString:@"editClassSegue"])
    {
        editClassViewController *vc = [segue destinationViewController];
        [vc giveInfoAtStart:classToEdit tableview:self];
    }
    else if([segue.identifier isEqualToString:@"gpaForecastSegue"])
    {
        gpaForcasterViewController *vc = [segue destinationViewController];
        [vc giveInfoAtStart:[creditsOverall.text floatValue] currentGPA:[GPAOverall.text floatValue]];
    }
    else{
        loginViewController *lc = [segue destinationViewController];
        [lc giveInfoAtStart:self];
    }
}


-(void) addClassViewController:(addClassViewController *)viewController addClass:(NSString *)name credits:(float)credits grade:(NSString *)grade
{
    [yearAndTermsArray[year][term] addClass:name credits:credits grade:grade];
    [self.navigationController popViewControllerAnimated:YES];
    [self dismissViewControllerAnimated:true completion:nil];
    [self saveClasses];
    [self reloadhome:true];
}


- (void)reloadhome:(BOOL) reloaddata
{
    if (reloaddata) {
        //[classTable reloadData];
        [classTable reloadSections:[NSIndexSet indexSetWithIndex:0] withRowAnimation:UITableViewRowAnimationAutomatic];
    }
    
    creditsThisTerm.text =[ NSString stringWithFormat:@"%g",[yearAndTermsArray[year][term] getTotalTermCredits:(true)]];
    creditsOverall.text = [ NSString stringWithFormat:@"%g",[self getTotalCredits:true]];
    GPAThisTerm.text = [NSString stringWithFormat:@"%.2f",[yearAndTermsArray[year][term] getTermGpa]];
    GPAOverall.text = [NSString stringWithFormat:@"%.2f",[self getTotalGPA]];
    
}



-(void)loadClasses
{
    
    AppDelegate *delegate = [[UIApplication sharedApplication] delegate];
    NSManagedObjectContext *context = [delegate managedObjectContext];
    NSEntityDescription *classDesc = [NSEntityDescription entityForName:@"Classes" inManagedObjectContext:context];
    NSFetchRequest *request = [[NSFetchRequest alloc] init];
    [request setEntity:classDesc];
    Classes *class = nil;
    NSError *error;
    NSArray *objects = [context executeFetchRequest:request error:&error];
    
    for (int i=0; i<[yearAndTermsArray count]; i++)
    {
        for (int j=0; j<[yearAndTermsArray[i] count]; j++)
        {
           // [yearAndTermsArray[year][term] removeAllObjects];
        }
    }
    
    for (int i =0; i<[objects count]; i++)
    {
        class = objects[i];
        int term = [class.termValue intValue];
        int year = [class.yearValue intValue];
        [yearAndTermsArray[year][term] addClass:class.classNameValue credits:[class.classCreditsValue floatValue] grade:class.classGradeValue];
    }
    [self reloadhome:true];
}


-(void)addClass:(NSString*)name grade:(NSString*)grade credits:(float)credits year:(int)year term:(int)term
{
    [yearAndTermsArray[year][term] addClass:name credits:credits grade:grade];
}

-(void)removeAllClasses
{
    for (int i=0; i<[yearAndTermsArray count]; i++)
    {
        for (int j=0; j<[yearAndTermsArray[i] count]; j++)
        {
                 [yearAndTermsArray[i][j] removeClasses];
        }
    }
}

-(void)saveClasses
{
    [self clearSaveData];
    AppDelegate *delegate = [[UIApplication sharedApplication] delegate];
    NSManagedObjectContext *context = [delegate managedObjectContext];
    
    for (int i=0; i<[yearAndTermsArray count]; i++)
    {
        for (int j=0; j<[yearAndTermsArray[i] count]; j++)
        {
            for (int k =0; k<[yearAndTermsArray[i][j] numOfClasses]; k++)
            {
                Classes *newClass = [NSEntityDescription insertNewObjectForEntityForName:@"Classes" inManagedObjectContext:context];
                newClass.yearValue = [NSNumber numberWithInt:i];
                newClass.termValue = [NSNumber numberWithInt:j];
                newClass.classNameValue = [[yearAndTermsArray[i][j] getClassObject:k] getName];
                newClass.classGradeValue = [[yearAndTermsArray[i][j] getClassObject:k] getGrade];
                newClass.classCreditsValue = [NSNumber numberWithFloat:[[yearAndTermsArray[i][j] getClassObject:k] getCredits]];
            }
        }
    }
    
    NSError *error;
    [context save:&error];
}


-(void)clearSaveData
{
    AppDelegate *delegate = [[UIApplication sharedApplication] delegate];
    NSManagedObjectContext *context = [delegate managedObjectContext];
    NSEntityDescription *classDesc = [NSEntityDescription entityForName:@"Classes" inManagedObjectContext:context];
    NSFetchRequest *request = [[NSFetchRequest alloc] init];
    [request setIncludesPropertyValues:NO];
    [request setEntity:classDesc];
    NSError *error;
    NSArray *objects = [context executeFetchRequest:request error:&error];

    
    for (NSManagedObject * class in objects) {
        [context deleteObject:class];
    }
    NSError *saveError = nil;
    [context save:&saveError];
}




- (float)getTotalCredits:(bool)includecr
{
    float totalcredits=0;
    for (int i=0; i<[yearAndTermsArray count]; i++)
    {
        for (int j=0; j<[yearAndTermsArray[i] count]; j++)
        {
            totalcredits += [yearAndTermsArray[i][j] getTotalTermCredits:includecr];
        }
    }
    return totalcredits;
}


- (float)getTotalGPA
{
    float GPA=0;
    
    for (int i=0; i<[yearAndTermsArray count]; i++)
    {
        for (int j=0; j<[yearAndTermsArray[i] count]; j++)
        {
            float termGpa =[yearAndTermsArray[i][j] getTermGpa];
            float termCredits =[yearAndTermsArray[i][j] getTotalTermCredits:false];
            float termPoints= termGpa * termCredits;
            GPA+= floorf(termPoints*100)/100;
        }
    }
    if([self getTotalCredits:false] !=0)
    {
        GPA = GPA/[self getTotalCredits:false];
       	GPA =  floorf(GPA * 100)/100;
    }
    else
    {
        GPA = 0.0;
    }
    return GPA;
}


- (IBAction)showPicker:(id)sender {
    [self.view layoutIfNeeded];
    [doneToolBarTopContraint setConstant:(-pickerview.frame.size.height)];
    [pickerViewTopContraint setConstant:(-pickerview.frame.size.height)];
    [UIView animateWithDuration:animationtime animations:^{
        [self.view layoutIfNeeded];
    }];
    [pickerview setUserInteractionEnabled:YES];
}

- (IBAction)doneButtonPress:(id)sender {
    year = (int)[pickerview selectedRowInComponent:0];
    term =(int)[pickerview selectedRowInComponent:1];
    [pickerview setUserInteractionEnabled:NO];
    [self.yearButton setTitle:self.yearArray[year] forState:UIControlStateNormal];
    [self.termButton setTitle:self.termArray[term] forState:UIControlStateNormal];
    [self.view layoutIfNeeded];
    [doneToolBarTopContraint setConstant:0];
    [pickerViewTopContraint setConstant:0];
    [UIView animateWithDuration:animationtime animations:^{
            [self.view layoutIfNeeded];
    }];
    
    [self reloadhome:true];
}


- (NSInteger)numberOfComponentsInPickerView:(UIPickerView *)pickerView
{
    return 2;  // returns the number of 'columns' to display.
}

- (NSInteger)pickerView:(UIPickerView *)pickerView numberOfRowsInComponent:(NSInteger)component
{
    if (component == 0) {
        return [yearArray count];
    }
    else{
        return [termArray count];
    }

}

- (NSString *)pickerView:(UIPickerView *)pickerView titleForRow:(NSInteger)row forComponent:(NSInteger)component
{
    if (component == 0) {
        return [yearArray objectAtIndex:row];
    }
    else{
        return [termArray objectAtIndex:row];
    }
    
}



- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return [yearAndTermsArray[year][term] numOfClasses];
}


- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {

    static NSString *cellIdentifier = @"Cell";
    
    SWTableViewCell *cell = (SWTableViewCell *)[tableView dequeueReusableCellWithIdentifier:cellIdentifier];
    
    if (cell == nil) {
        
        cell = [[SWTableViewCell alloc] initWithStyle:UITableViewCellStyleSubtitle reuseIdentifier:cellIdentifier];
        cell.rightUtilityButtons = [self rightButtons];
        cell.delegate = self;}
    
    
    cell.textLabel.text = [yearAndTermsArray[year][term] getClass:(int)indexPath.row];
    cell.backgroundColor = [UIColor clearColor];
    cell.textLabel.backgroundColor = [UIColor clearColor];
    cell.detailTextLabel.backgroundColor = [UIColor clearColor];
    return cell;
    
}


- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    [tableView deselectRowAtIndexPath:indexPath animated:YES];
}



-(void)setupYearAndTermsArray
{
    CGSize result = [[UIScreen mainScreen] bounds].size;
    UIImageView *bg;
    if(result.height == 480)
    {
       bg = [[UIImageView alloc] initWithImage:[UIImage imageNamed:@"background_iphone4.png"]];
    }
    else
    {
        bg = [[UIImageView alloc] initWithImage:[UIImage imageNamed:@"background_iphone5.png"]];
    }
    [classTable setBackgroundView:bg];
    
    [pickerview setUserInteractionEnabled:NO];
    yearArray = [[NSMutableArray alloc] initWithObjects:@"Freshman", @"Sophomore", @"Pre-Junior", @"Junior",@"Senior", nil];
    termArray = [[NSMutableArray alloc] initWithObjects:@"Fall", @"Winter", @"Spring", @"Summer", nil];
    GPAOverall.text = @"0";
    GPAThisTerm.text = @"0";
    creditsOverall.text = @"0";
    creditsThisTerm.text = @"0";
    year = 0;
    term = 0;

  yearAndTermsArray = [[NSMutableArray alloc] initWithCapacity: 5];
    for (int i=0; i< 5; i++) {
        
        [yearAndTermsArray insertObject:[NSMutableArray arrayWithObjects:[[termClasses alloc] init],[[termClasses alloc] init],[[termClasses alloc] init],[[termClasses alloc] init], nil] atIndex:i];
    }
    [self loadClasses];
    [self reloadhome:true];
}


- (NSArray *)rightButtons
{
    NSMutableArray *rightUtilityButtons = [NSMutableArray new];
    [rightUtilityButtons sw_addUtilityButtonWithColor:
     [UIColor colorWithRed:0.78f green:0.78f blue:0.8f alpha:1.0]
                                                title:@"Edit"];
    [rightUtilityButtons sw_addUtilityButtonWithColor:
     [UIColor colorWithRed:1.0f green:0.231f blue:0.188 alpha:1.0f]
                                                title:@"Delete"];
    
    return rightUtilityButtons;
}

- (void)swipeableTableViewCell:(SWTableViewCell *)cell didTriggerRightUtilityButtonWithIndex:(NSInteger)index
{
    NSIndexPath *cellIndexPath = [self.classTable indexPathForCell:cell];
    if (index == 1)
    {
        
        
        
        NSIndexPath *cellIndexPath = [classTable indexPathForCell:cell];
        
        [yearAndTermsArray[year][term] removeClass:(int)cellIndexPath.row];
        [CATransaction begin];
        [classTable beginUpdates];
        [CATransaction setCompletionBlock: ^{
            [self reloadhome:false];
        }];
        [classTable deleteRowsAtIndexPaths:@[cellIndexPath] withRowAnimation:UITableViewRowAnimationFade];
        [classTable endUpdates];
         [CATransaction commit];
        [self saveClasses];
        
    }
    else
    {
        classToEdit = [yearAndTermsArray[year][term] getClassObject:(int)cellIndexPath.row];
        [self performSegueWithIdentifier: @"editClassSegue" sender: self];
        
    }
        
}

- (BOOL)swipeableTableViewCellShouldHideUtilityButtonsOnSwipe:(SWTableViewCell *)cell {
    return YES;
}


@end

