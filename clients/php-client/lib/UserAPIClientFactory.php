<?php

namespace Airavata\UserAPI\Client;

$GLOBALS['THRIFT_ROOT'] = 'Thrift/';
require_once $GLOBALS['THRIFT_ROOT'] . 'Transport/TTransport.php';
require_once $GLOBALS['THRIFT_ROOT'] . 'Transport/TSocket.php';
require_once $GLOBALS['THRIFT_ROOT'] . 'Protocol/TProtocol.php';
require_once $GLOBALS['THRIFT_ROOT'] . 'Protocol/TBinaryProtocol.php';
require_once $GLOBALS['THRIFT_ROOT'] . 'Exception/TException.php';
require_once $GLOBALS['THRIFT_ROOT'] . 'Exception/TTransportException.php';
require_once $GLOBALS['THRIFT_ROOT'] . 'Type/TType.php';
require_once $GLOBALS['THRIFT_ROOT'] . 'Type/TMessageType.php';
require_once $GLOBALS['THRIFT_ROOT'] . 'Factory/TStringFuncFactory.php';
require_once $GLOBALS['THRIFT_ROOT'] . 'StringFunc/TStringFunc.php';
require_once $GLOBALS['THRIFT_ROOT'] . 'StringFunc/Core.php';

$GLOBALS['AIRAVATA_ROOT'] = 'Airavata/';
require_once $GLOBALS['AIRAVATA_ROOT'] . 'UserAPI/Airavata.php';

use Thrift\Protocol\TBinaryProtocol;
use Thrift\Transport\TSocket;
use Airavata\UserAPI\UserAPIClient;

class UserAPIClientFactory
{

    private $userapiServerHost;
    private $userapiServerPort;

    public function __construct($options)
    {
        $this->userapiServerHost = isset($options['userapiServerHost']) ? $options['userapiServerHost'] : null;
        $this->userapiServerPort = isset($options['userapiServerPort']) ? $options['userapiServerPort'] : null;
    }

    public function getUserAPIClient()
    {
        $transport = new TSocket($this->userapiServerHost, $this->userapiServerPort);
        $protocol = new TBinaryProtocol($transport);
        $transport->open();
        return new UserAPIClient($protocol);
    }
}