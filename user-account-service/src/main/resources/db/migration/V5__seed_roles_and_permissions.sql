INSERT INTO roles (name, created_at, updated_at) VALUES
                                                     ('CUSTOMER', NOW(), NOW()),
                                                     ('ADMIN', NOW(), NOW()),
                                                     ('AUDITOR', NOW(), NOW());

INSERT INTO permissions (value, created_at, updated_at) VALUES
                                                            ('INITIATE_TRANSFER', NOW(), NOW()),
                                                            ('VIEW_OWN_TRANSACTIONS', NOW(), NOW()),
                                                            ('VIEW_ALL_TRANSACTIONS', NOW(), NOW()),
                                                            ('MANAGE_ACCOUNTS', NOW(), NOW()),
                                                            ('FREEZE_ACCOUNT', NOW(), NOW()),
                                                            ('VIEW_FRAUD_CASES', NOW(), NOW()),
                                                            ('MANAGE_BENEFICIARIES', NOW(), NOW());

-- CUSTOMER permissions
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id FROM roles r
                           JOIN permissions p ON p.value IN (
                                                             'INITIATE_TRANSFER',
                                                             'VIEW_OWN_TRANSACTIONS',
                                                             'MANAGE_BENEFICIARIES'
    )
WHERE r.name = 'CUSTOMER';

-- ADMIN gets all permissions
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id FROM roles r
                           CROSS JOIN permissions p
WHERE r.name = 'ADMIN';

-- AUDITOR permissions
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id FROM roles r
                           JOIN permissions p ON p.value IN (
                                                             'VIEW_OWN_TRANSACTIONS',
                                                             'VIEW_ALL_TRANSACTIONS',
                                                             'VIEW_FRAUD_CASES'
    )
WHERE r.name = 'AUDITOR';