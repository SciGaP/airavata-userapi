<?php

namespace Airavata\UserAPI\Client\Samples;

$GLOBALS['THRIFT_ROOT'] = '../lib/Thrift/';
require_once $GLOBALS['THRIFT_ROOT'] . 'Transport/TTransport.php';
require_once $GLOBALS['THRIFT_ROOT'] . 'Transport/TSocket.php';
require_once $GLOBALS['THRIFT_ROOT'] . 'Protocol/TProtocol.php';
require_once $GLOBALS['THRIFT_ROOT'] . 'Protocol/TBinaryProtocol.php';
require_once $GLOBALS['THRIFT_ROOT'] . 'Exception/TException.php';
require_once $GLOBALS['THRIFT_ROOT'] . 'Exception/TApplicationException.php';
require_once $GLOBALS['THRIFT_ROOT'] . 'Exception/TProtocolException.php';
require_once $GLOBALS['THRIFT_ROOT'] . 'Exception/TTransportException.php';
require_once $GLOBALS['THRIFT_ROOT'] . 'Base/TBase.php';
require_once $GLOBALS['THRIFT_ROOT'] . 'Type/TType.php';
require_once $GLOBALS['THRIFT_ROOT'] . 'Type/TMessageType.php';
require_once $GLOBALS['THRIFT_ROOT'] . 'Factory/TStringFuncFactory.php';
require_once $GLOBALS['THRIFT_ROOT'] . 'StringFunc/TStringFunc.php';
require_once $GLOBALS['THRIFT_ROOT'] . 'StringFunc/Core.php';

$GLOBALS['AIRAVATA_ROOT'] = '../lib/Airavata/';
require_once $GLOBALS['AIRAVATA_ROOT'] . 'UserAPI/UserAPI.php';
require_once $GLOBALS['AIRAVATA_ROOT'] . 'UserAPI/Types.php';
require_once $GLOBALS['AIRAVATA_ROOT'] . 'UserAPI/Models/Types.php';


use Airavata\UserAPI\Models\AuthenticationResponse;
use Airavata\UserAPI\Models\APIPermissions;
use Airavata\UserAPI\Models\UserProfile;
use Airavata\UserAPI\Error\UserAPISystemException;
use Airavata\UserAPI\Error\InvalidRequestException;
use Airavata\UserAPI\Error\AuthorizationException;
use Airavata\UserAPI\Error\AuthenticationException;
use Airavata\UserAPI\Error\TimedOutException;

use Airavata\UserAPI\Client\AiravataClientFactory;
use Thrift\Protocol\TBinaryProtocol;
use Thrift\Transport\TSocket;
use Airavata\UserAPI\UserAPIClient;

$userapiconfig = parse_ini_file("userapi-client-properties.ini");

$transport = new TSocket($userapiconfig['USERAPI_SERVER'], $userapiconfig['USERAPI_PORT']);
$transport->setRecvTimeout($userapiconfig['USERAPI_TIMEOUT']);

$protocol = new TBinaryProtocol($transport);
$transport->open();
$client = new UserAPIClient($protocol);

try
{
    $token = $client->authenticateGateway($userapiconfig['ADMIN_USERNAME'],$userapiconfig['ADMIN_PASSWORD']);
    if($token !== null){
        if($client->checkUsernameExists("test_user", $token->accessToken)){
            print "Username \"test_user\" already exists" . "\n";
        }else{
            print "Username \"test_user\" doesn't exists" . "\n";
        }

    }else{
        print "Invalid credential for the Admin" . "\n";
        exit;
    }
}
catch (TException $texp)
{
    print "Exception: " . $texp->getMessage()."\n";
}
