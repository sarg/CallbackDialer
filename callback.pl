#!/usr/bin/perl

use strict;
use CGI qw/:standard/;
use File::Temp qw/tempfile/;
use File::Copy;
use Data::Dumper;

my $req = new CGI;

if ($req->request_method ne 'POST') {
    print header(-type => 'text/html', -status => '403');
    exit;
}

print header;

my ($fh, $fn)  = tempfile();
my $form = sprintf <<EOF
Channel: Local/%s\@default
Extension: %s
Context: default
Priority: 1
EOF
, $req->url_param('from'),
  $req->url_param('to');

print $fh $form;
close $fh;

chmod(0660, $fn);
move $fn, "/var/spool/asterisk/outgoing/"
    || print $!;
