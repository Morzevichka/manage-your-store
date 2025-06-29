create pluggable database clothing_shop_pdb
    admin shop_admin identified by anx00di1
    roles = (DBA)
    file_name_convert = ('C:\app\solov\product\21c\oradata\XE\pdbseed',
        'C:\app\solov\product\21c\oradata\XE\clothing_shop_pdb')
    store unlimited
    path_prefix = 'C:\app\solov\product\21c\oradata\XE\clothing_shop_pdb';

alter pluggable database clothing_shop_pdb open;