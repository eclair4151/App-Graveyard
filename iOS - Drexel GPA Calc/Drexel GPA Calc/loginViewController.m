//
//  loginViewController.m
//  Drexel GPA Calc
//
//  Created by Tomer Shemesh on 7/24/14.
//  Copyright (c) 2014 Tomer Shemesh. All rights reserved.
//

#import "loginViewController.h"
#import "AFNetworking.h"
#import "HTMLParser.h"

@interface loginViewController ()

@end

@implementation loginViewController
@synthesize passwordBox,usernameBox, progressSpinner, resultBox;

ViewController *classTable;


- (void)viewDidLoad
{
    [super viewDidLoad];
    passwordBox.delegate = self;
    usernameBox.delegate = self;
    

}

- (IBAction)closeButtonAction:(id)sender {
    [self dismissViewControllerAnimated:YES completion:nil];
}

-(void) stopSpinner
{
    [progressSpinner stopAnimating];
    [resultBox setContentInset:UIEdgeInsetsMake(0,0,0,0)];
}
- (IBAction)getClassesButtonAction:(id)sender {
    [usernameBox resignFirstResponder];
    [passwordBox resignFirstResponder];
    [progressSpinner startAnimating];
    [resultBox setContentInset:UIEdgeInsetsMake(20,0,0,0)];
    [resultBox setText:@""];
    [self makeRequest];
}

- (BOOL)textFieldShouldReturn:(UITextField *)textField
{
    [textField resignFirstResponder];
    return YES;
}


- (void)giveInfoAtStart:(ViewController *)classtable
{

    classTable = classtable;
}


- (void)makeRequest
{
    NSArray *cookies = [[NSHTTPCookieStorage sharedHTTPCookieStorage] cookies];
    for (NSHTTPCookie *cookie in cookies)
    {
        [[NSHTTPCookieStorage sharedHTTPCookieStorage] deleteCookie:cookie];
    }
    
    NSString* URL = @"https://login.drexel.edu/cas/login";
    
    AFHTTPRequestOperationManager *manager = [AFHTTPRequestOperationManager manager];
    manager.responseSerializer = [AFHTTPResponseSerializer serializer];
    manager.securityPolicy.allowInvalidCertificates = YES;
    
    [manager GET:URL parameters:nil success:^(AFHTTPRequestOperation *operation, id responseObject) {
        NSString *string = [[NSString alloc] initWithData:responseObject encoding:NSASCIIStringEncoding];
        [self getLT:string];
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        [resultBox setText:@"Error loading login page"];
        [self stopSpinner];
    }];
}


- (void)getLT:(NSString*)html
{
    NSError* error = nil;
    HTMLParser *parser = [[HTMLParser alloc] initWithString:html error:&error];
    HTMLNode* body = [parser body];
    HTMLNode* nodes = [body findChildOfClass:@"row btn-row"];
    NSArray* inputNodes = [nodes findChildTags:@"input"];
    
    NSString* lt=nil;
    NSString* excecution=nil;
    for (HTMLNode *inputNode in inputNodes) {
        if ([[inputNode getAttributeNamed:@"name"] isEqualToString:@"lt"] ) {
            lt =[inputNode getAttributeNamed:@"value"];
        }
        else if ([[inputNode getAttributeNamed:@"name"] isEqualToString:@"execution"])
        {
            excecution = [inputNode getAttributeNamed:@"value"];
        }
    }
    [self login:lt exc:excecution];
    
}

- (void)login:(NSString*)lt exc:(NSString*)excecution
{
    NSHTTPCookie *cookies = [[NSHTTPCookieStorage sharedHTTPCookieStorage] cookies][0];
    NSString *name = [cookies name];
    NSString *value = [cookies value];
    NSString *loginUrl = [NSString stringWithFormat:@"https://login.drexel.edu/cas/login;%@=%@", name, value];
    NSDictionary *parameters = @{@"username": [usernameBox text], @"password": [passwordBox text],@"lt": lt,@"execution": excecution, @"_eventId": @"submit", @"submit": @"Connect" };
    
    
    AFHTTPRequestOperationManager *manager = [AFHTTPRequestOperationManager manager];
    manager.responseSerializer = [AFHTTPResponseSerializer serializer];
    [manager POST:loginUrl parameters:parameters success:^(AFHTTPRequestOperation *operation, id responseObject) {
        NSString *string = [[NSString alloc] initWithData:responseObject encoding:NSASCIIStringEncoding];
        if([string rangeOfString:@"The credentials you provided cannot be determined to be authentic."].location != NSNotFound || [string rangeOfString:@"is a required field."].location != NSNotFound)
        {
            [resultBox setText:@"Invalid Username or Password"];
            [self stopSpinner];
        }
        else
        {
            [self getTranscriptPage];
            
        }
        
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        NSLog(@"Error: %@", error);
    }];
}

- (void) getTranscriptPage
{
    NSString *URL = @"https://bannersso.drexel.edu/ssomanager/c/SSB?pkg=bwszkfrag.P_DisplayFinResponsibility%3Fi_url%3Dbwskotrn.P_ViewTermTran";
    
    AFHTTPRequestOperationManager *manager = [AFHTTPRequestOperationManager manager];
    manager.responseSerializer = [AFHTTPResponseSerializer serializer];
    manager.securityPolicy.allowInvalidCertificates = YES;
    [manager GET:URL parameters:nil success:^(AFHTTPRequestOperation *operation, id responseObject) {
        //NSString *string = [[NSString alloc] initWithData:responseObject encoding:NSASCIIStringEncoding];
        [self getTranscript];
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        [resultBox setText:@"Error loading transcript selection page"];
        [self stopSpinner];
    }];
}

- (void) getTranscript
{
    NSString *URL = @"https://banner.drexel.edu/pls/duprod/bwskotrn.P_ViewTran";
    NSDictionary *parameters = @{@"levl": @"", @"tprt": @"STUD" };
    
    AFHTTPRequestOperationManager *manager = [AFHTTPRequestOperationManager manager];
    manager.responseSerializer = [AFHTTPResponseSerializer serializer];
    manager.securityPolicy.allowInvalidCertificates = YES;
    
    [manager POST:URL parameters:parameters success:^(AFHTTPRequestOperation *operation, id responseObject) {
        NSString *string = [[NSString alloc] initWithData:responseObject encoding:NSASCIIStringEncoding];
        [self parseTranscript:string];
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        [resultBox setText:@"Error loading transcript"];
        [self stopSpinner];
    }];
}
- (IBAction)tappedScreen:(id)sender {
    [usernameBox resignFirstResponder];
    [passwordBox resignFirstResponder];
}

-(void)parseTranscript:(NSString*) html
{
    [resultBox setText:@""];
    NSError* error = nil;
    HTMLParser *parser = [[HTMLParser alloc] initWithString:html error:&error];
    HTMLNode* body = [parser body];
    NSArray* nodes = [[body findChildOfClass:@"datadisplaytable"]findChildTags:@"tr"];
    int index = 0;
    while([[nodes[index] rawContents] rangeOfString:@"INSTITUTION CREDIT"].location == NSNotFound)
    {
        index++;
    }
    int year = -1;
    NSString* currentYear = @"";
    [classTable removeAllClasses];
    
    while([[nodes[index] rawContents] rangeOfString:@"COURSES IN PROGRESS"].location == NSNotFound)
    {
        if([[nodes[index] rawContents] rangeOfString:@"fieldOrangetextbold"].location != NSNotFound)
        {
            int term = 0;
            NSString* yearTerm = [[nodes[index] findChildOfClass:@"fieldOrangetextbold"] contents];
            if([yearTerm rangeOfString:@"Winter"].location != NSNotFound)
            {
                term = 1;
            }
            else if([yearTerm rangeOfString:@"Spring"].location != NSNotFound)
            {
                term = 2;
            }
            else if([yearTerm rangeOfString:@"Summer"].location != NSNotFound)
            {
                term = 3;
            }
            
            
            if (![currentYear isEqualToString:[yearTerm substringFromIndex: [yearTerm length] - 2]])
            {
                year++;
                currentYear = [yearTerm substringFromIndex: [yearTerm length] - 2];
            }
            
            while([[nodes[index] rawContents] rangeOfString:@"Subject"].location == NSNotFound)
            {
                index++;
            }
            index++;
            
            NSString *prev =[resultBox text];
            [resultBox setText:[NSString stringWithFormat:@"%@%@",prev,yearTerm]];
            while([[nodes[index] rawContents] rangeOfString:@"Term Totals"].location == NSNotFound)
            {
                NSArray* classparts = [nodes[index] findChildrenOfClass:@"dddefault"];
                NSString* name = [NSString stringWithFormat:@"%@%@", [classparts[0] contents], [classparts[1] contents]];
                NSString * grade = [classparts[4] contents];
                NSString *creditString = [[classparts[5] findChildOfClass:@"rightaligntext"] contents];
                creditString = [creditString stringByTrimmingCharactersInSet: [NSCharacterSet whitespaceCharacterSet]];
                float credit = (CGFloat)[creditString floatValue];
                
                [classTable addClass:name grade:grade credits:credit year:year term:term];
                
                NSString *prev =[resultBox text];
                [resultBox setText:[NSString stringWithFormat:@"%@\n%@   %g   %@",prev,name,credit,grade]];
                index++;
            }
            prev =[resultBox text];
            [resultBox setText:[NSString stringWithFormat:@"%@\n\n",prev]];
            
        }
        else{
            index++;
        }
        
    }
    [classTable reloadhome:true];
    [classTable saveClasses];
    [self stopSpinner];
    
}


@end