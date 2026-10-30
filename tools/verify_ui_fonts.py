#!/usr/bin/env python3
"""Verify pinned unmodified static fonts, complete OFL notices and required script glyphs."""
from pathlib import Path
import hashlib,json,struct
root=Path(__file__).resolve().parents[1]
def u16(b,o): return struct.unpack_from('>H',b,o)[0]
def u32(b,o): return struct.unpack_from('>I',b,o)[0]
def glyph(b,cp,tables):
 start=tables[b'cmap'];subs=[start+u32(b,start+4+i*8+4) for i in range(u16(b,start+2))]
 for sub in subs:
  fmt=u16(b,sub)
  if fmt==12:
   for i in range(u32(b,sub+12)):
    a,z,g=struct.unpack_from('>III',b,sub+16+12*i)
    if a<=cp<=z and g+cp-a: return True
  if fmt==4 and cp<=0xffff:
   n=u16(b,sub+6)//2;ends=sub+14;starts=ends+2*n+2;deltas=starts+2*n;ranges=deltas+2*n
   for i in range(n):
    if u16(b,starts+2*i)<=cp<=u16(b,ends+2*i):
     off=u16(b,ranges+2*i);delta=u16(b,deltas+2*i)
     g=(cp+delta)&0xffff if off==0 else u16(b,ranges+2*i+off+2*(cp-u16(b,starts+2*i)))
     if off and g:g=(g+delta)&0xffff
     if g:return True
 return False
for e in json.loads((root/'third_party/ui-font-provenance.json').read_text())['files']:
 b=(root/e['file']).read_bytes();assert hashlib.sha256(b).hexdigest()==e['sha256']
 if e['file'].endswith('.txt'):assert b'SIL OPEN FONT LICENSE Version 1.1' in b;continue
 tables={b[12+16*i:16+16*i]:u32(b,20+16*i) for i in range(u16(b,4))};assert b'fvar' not in tables,'Variable font is not approved for KitKat'
 latin='ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789'
 sample='ابتثجحخدذرزسشصضطظعغفقكلمنهويأإآءؤئىةًٌٍَُِّْٰ٠١٢٣٤٥٦٧٨٩'+latin if 'Lato' not in e['file'] else latin
 assert all(glyph(b,ord(c),tables) for c in sample),'Missing required glyph'
 if 'Lato' not in e['file']:assert b'GSUB' in tables and b'GPOS' in tables
print('PASS: static unmodified OFL fonts/notices match pinned hashes; Arabic shaping tables/diacritics and Latin glyph coverage present. Device rendering remains a runtime test.')
