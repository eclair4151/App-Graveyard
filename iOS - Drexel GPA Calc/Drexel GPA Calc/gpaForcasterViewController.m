//
//  gpaForcasterViewController.m
//  Drexel GPA Calc
//
//  Created by Tomer Shemesh on 5/8/14.
//  Copyright (c) 2014 Tomer Shemesh. All rights reserved.
//

#import "gpaForcasterViewController.h"
#import <math.h>
@interface gpaForcasterViewController ()

@end

@implementation gpaForcasterViewController
@synthesize gpaForcastTotalCredits,gpaForecastCurrentGpa,gpaForecastDesiredGpa,gpaForecastResultLabel,gpaForecastTpToDismiss;
float totalClassCredits,currentClassGPA;

- (void)viewDidLoad
{
    [super viewDidLoad];
    gpaForecastTpToDismiss.cancelsTouchesInView = NO;
    [gpaForcastTotalCredits setKeyboardType:UIKeyboardTypeDecimalPad];
    [gpaForecastCurrentGpa setKeyboardType:UIKeyboardTypeDecimalPad];
    [gpaForecastDesiredGpa setKeyboardType:UIKeyboardTypeDecimalPad];
    
    gpaForcastTotalCredits.text = [NSString stringWithFormat:@"%g", totalClassCredits];
    gpaForecastCurrentGpa.text = [NSString stringWithFormat:@"%g", currentClassGPA];
    gpaForecastResultLabel.text = @"";
    
    // Do any additional setup after loading the view.
}


- (IBAction)gpaForecastCloseButtonPress:(id)sender
{
    [self dismissViewControllerAnimated:true completion:nil];
}

- (void)giveInfoAtStart:(float)totalCredits currentGPA:(float)currentGPA
{
    totalClassCredits = totalCredits;
    currentClassGPA = currentGPA;
}

- (IBAction)gpaForecastTapToDismissTapped:(id)sender
{
    [self dismissKeyboard];
}

- (IBAction)gpeForecastCalculateButtonPressed:(id)sender
{
    
    
    
    gpaForecastResultLabel.text = @"";
    float desiredGPA = [gpaForecastDesiredGpa.text floatValue];
    totalClassCredits = [ gpaForcastTotalCredits.text floatValue];
    currentClassGPA = [gpaForecastCurrentGpa.text floatValue];
    
    
    
    if ([gpaForecastDesiredGpa.text isEqualToString:@""] || [gpaForecastCurrentGpa.text isEqualToString:@""] ||[gpaForcastTotalCredits.text isEqualToString:@""])
    {
        gpaForecastResultLabel.text=@"Error: One or more fields are blank";
    }
    else if([gpaForecastDesiredGpa.text floatValue] > 4.0)
    {
        gpaForecastResultLabel.text=@"Error: Desired GPA cannot be more than 4.0";
    }
    else if([gpaForecastDesiredGpa.text floatValue] <[gpaForecastCurrentGpa.text floatValue])
    {
         gpaForecastResultLabel.text=@"Error: Desired GPA cannot be less then current GPA";
    }
    else
    {
    float posssGPA = 4.0;
    
    float creditsNeeded = totalClassCredits*(desiredGPA-currentClassGPA) / (posssGPA - desiredGPA);
    if(creditsNeeded >120)
    {
        gpaForecastResultLabel.text=@"Error: Impossible to Obtain in 120 credits or less";
    }
    
    
    while (posssGPA > desiredGPA && creditsNeeded <= 120)
    {
        NSString *prev =gpaForecastResultLabel.text;
        gpaForecastResultLabel.text= [NSString stringWithFormat:@"%@%g credits with a GPA of %g\n",prev,ceil(creditsNeeded),posssGPA];
        posssGPA -= 0.1;
        creditsNeeded = totalClassCredits*(desiredGPA-currentClassGPA) / (posssGPA - desiredGPA);
    }
    }
    
}

-(void)dismissKeyboard
{
    [gpaForcastTotalCredits resignFirstResponder];
    [gpaForecastCurrentGpa resignFirstResponder];
    [gpaForecastDesiredGpa resignFirstResponder];
}

@end
