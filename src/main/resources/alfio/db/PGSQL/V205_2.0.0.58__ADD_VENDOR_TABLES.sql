--
-- This file is part of alf.io.
--
-- alf.io is free software: you can redistribute it and/or modify
-- it under the terms of the GNU General Public License as published by
-- the Free Software Foundation, either version 3 of the License, or
-- (at your option) any later version.
--
-- alf.io is distributed in the hope that it will be useful,
-- but WITHOUT ANY WARRANTY; without even the implied warranty of
-- MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
-- GNU General Public License for more details.
--
-- You should have received a copy of the GNU General Public License
-- along with alf.io.  If not, see <http://www.gnu.org/licenses/>.
--

CREATE EXTENSION IF NOT EXISTS "pgcrypto";

-- Create accounts table
CREATE TABLE IF NOT EXISTS accounts (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    subs TEXT[] NOT NULL DEFAULT '{}',
    email TEXT NOT NULL UNIQUE,
    name TEXT NOT NULL,
    nickname TEXT,
    picture TEXT,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- Create vendor_booth_types table
CREATE TABLE IF NOT EXISTS vendor_booth_types (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid (),
    name TEXT NOT NULL,
    description TEXT,
    event_id INTEGER NOT NULL REFERENCES event (id) ON DELETE CASCADE;

organization_id INTEGER NOT NULL REFERENCES organization (id) ON DELETE CASCADE;

price NUMERIC(10, 2) NOT NULL );

-- Create vendor_applications table
CREATE TABLE IF NOT EXISTS vendor_applications (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid (),
    applicant_name TEXT NOT NULL,
    store_name TEXT NOT NULL,
    email TEXT NOT NULL,
    phone_number TEXT,
    description TEXT,
    instagram TEXT,
    portfolio TEXT,
    event_id INTEGER NOT NULL REFERENCES event (id) ON DELETE CASCADE;

organization_id INTEGER NOT NULL REFERENCES organization (id) ON DELETE CASCADE;

booth_type_id UUID REFERENCES vendor_booth_types (id) ON DELETE SET NULL,
    status TEXT NOT NULL DEFAULT 'pending',
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- Alter tickets_reservation table to add a foreign key to accounts
ALTER TABLE tickets_reservation
ADD COLUMN IF NOT EXISTS account_id UUID,
ADD CONSTRAINT fk_account FOREIGN KEY (account_id) REFERENCES accounts (id) ON DELETE SET NULL;