REM producer that sends 10 msg / [s] - send every 100 ms, hostname 127.0.0.1, port 2555, seednode 127.0.0.1:2555, config application_round_robin_router.conf
call cmd /c start producer.cmd 100 127.0.0.1 2555 127.0.0.1:2555 application_round_robin_router.conf
REM consumer that receives max. 4 msg / [s], processing time 250 ms, hostname 127.0.0.1, port 2556, seednode 127.0.0.1:2555, config application_round_robin_router.conf
call cmd /c start consumer.cmd 250 127.0.0.1 2556 127.0.0.1:2555 application_round_robin_router.conf
REM consumer that receives max. 4 msg / [s]
call cmd /c start consumer.cmd 250 127.0.0.1 2557 127.0.0.1:2555 application_round_robin_router.conf
REM consumer that receives max. 2 msg / [s]
call cmd /c start consumer.cmd 500 127.0.0.1 2558 127.0.0.1:2555 application_round_robin_router.conf
REM consumer that receives max. 1 msg / [s]
call cmd /c start consumer.cmd 1000 127.0.0.1 2559 127.0.0.1:2555 application_round_robin_router.conf
