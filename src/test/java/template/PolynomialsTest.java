�]�K���S�ˏfvf�I�J��	Җɯ���'��tS�oB�VvĠ���P����lU�Ϣ a��i���������s�"%�rp�,�v�����.�Bt�v��rދN)��|�pR�8g<\���S��n�s�ܶ�E����U�>�?�_H��ToLl���Pa}��16�_����g> \����:.�	<��$|Vb��J���W���1�w9қR�M��Oɠ�=P2~�I�!�͆���� �8����-�$���R� �|=��$&`g����|ۧT>�^Q�7�oiTo��s��-��>x�;;��v�[�^t�Z8Z"I�+�tI ��/�Q����]����u��K`�ӏ� �:�mW�����K��
N:��,ّ�>��`��O�󟪕��΀��4p�F��ɿ4>U��x�*�D���	�@g��b��3�=	٪��r?�L��s�~Y���U�����i� �:yo8b�/?g����.GI?�w�hF��8�����Vb�I����� O�o_������Ҩ����ח���9����d\f��{(��}�O�ϣ�Tg3�7�k�D�%�O��6#ؤ�=�w�ڟp�4��I=�X�K��D�����Z�V��8`�������Ԋ���-8��m��*[���&\��IrV�
��������	_qt!�c �o�~��'*���-,9�Bˑ� 6m'�Y&��K�k?�>����7<	�,F5�W�3\vq�O��ƁI�7i���%8QQ��������h�E�)�Z��8�^�GqڂRPlz�� ����EpH[����_�sZ�cE�<l�R*���uĵߥ�
��	���R�;�L<��k�2�$̶��b�wn!�r�sq��J�~'���u�Du�մ߂#9j����	{�>~`V�@���L��Vv�Ǡh�<��&�c��}P�?�l�Y�DJZ��q�) �>�f@j1N�e���b������&��غe�c�~?a����_*�ty��o	��T�k8��
ܶk�����c� zr!�0#��L�9_�|�G������0ewH�ì�^�y�5���]��{ �~!p�ѫ���뾔��|q��<��B}'����G��*f�S�iH3��>��q����}�hT��������h� 
�3����
���g�$9�
AC見h?mM�05��c��oQ��G��t૾�$
���]�-;?��=.��dಓ�G,vh4�R-�����D�d��Z�PdԪ��\��0o�R�he�1���w�|h׷/SEj"�&�wI,����)C���ܞDBѦr�
�!d��붐�3:k��<��{��Y��<N/7�~�����N4 f���W���u^�؀�a�"[�2�� �gɡ4}i�e�ۆ7�1ȱ�ױ4k`���lCf��47aa3.��HZ�bDȔ�1F�߱	�������������b��Ng���ƚ���2U���`�-տ�@6���d�Բ�"0�C?͕��3���$9�?aC2S�¢"�5�s�(iq���	��F�R��l#��]'�t�!�z�]��3��YSuS���H=q7�;ZI'v ,�NG=�"h�<Ĳc�a y����
�-?rI�lB�NA�LHԵ����W���[�c wXQ;�����BiŁ{���{&p�����ry�^���^�<<���h��6���TU�h�03�)�ӳ������؈'�;&��[b/��Xy?����(r��2����C�	sAS+�~;��*����$�"~��Y�;/����~��^~�ق������U�d�ת�/@�X�ځhd�K}�~���=ـ~����|L�.���1�B	cUˏ"G�Y�<~~�� ��Eq:9�    IEND�B`�                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                             }

    @Test
    public void test7() {
        int[] p = new int[]{1, 1, 0, 0, 0, 0, 0, 0};
        int[] remainder = getPoly().module(2, p);

        Assert.assertTrue(equal(new int[]{1, 0, 0, 0, 0, 0, 0, 0}, remainder));
        PrimitiveBuffers.release(remainder);
    }

    @Test
    public void test8() {
        int m = 2;
        int[] p = new int[1 << m];
        p[1] = 1;

        int[] clone = p.clone();
        NumberTheoryTransform.ntt(p, false, getMod().getMod(), 3, new Power(getMod().getMod()));
        NumberTheoryTransform.ntt(p, true, getMod().getMod(), 3, new Power(getMod().getMod()));

        Assert.assertTrue(equal(clone, p));
    }

    @Test
    public void test9() {

        int[][] lists = new int[3][];
        lists[0] = PrimitiveBuffers.allocIntPow2(SequenceUtils.wrapArray(1, 0, 1));
        lists[1] = PrimitiveBuffers.allocIntPow2(SequenceUtils.wrapArray(0, 1, 0));
        lists[2] = PrimitiveBuffers.allocIntPow2(SequenceUtils.wrapArray(2));

        int[] ans = getPoly().dacMul(lists);
        Assert.assertTrue(equal(SequenceUtils.wrapArray(0, 2, 0, 2), ans));
        PrimitiveBuffers.release(ans);
        PrimitiveBuffers.release(lists);
    }

    @Test
    public void test13() {
        int[] p = new int[]{1, 2, 3, 4, 5, 6, 7, 8};
        int[] inv = getPoly().inverse(p, 8);
        int[] c = getPoly().modmul(p, inv, 8);
        Assert.assertTrue(equal(SequenceUtils.wrapArray(1), c));
        PrimitiveBuffers.release(inv);
        PrimitiveBuffers.release(c);
    }

    @Test
    public void test15() {
        int[] p = new int[]{1, 2, 3};
        int[] inv = getPoly().inverse(p, 3);
        int[] c = getPoly().modmul(p, inv, 3);
        Assert.assertTrue(equal(SequenceUtils.wrapArray(1), c));
        PrimitiveBuffers.release(inv);
        PrimitiveBuffers.release(c);
    }

    @Test
    public void testInv() {
        int[] p = new int[]{1, 0};
        int[] inv = getPoly().inverse(p, 2);
        int[] c = getPoly().modmul(p, inv, 2);
        Assert.assertTrue(equal(SequenceUtils.wrapArray(1), c));
        PrimitiveBuffers.release(inv);
        PrimitiveBuffers.release(c);
    }

    @Test
    public void test14() {
        int[] p = new int[]{1, 2, 3, 4, 5, 6, 7, 8};
        int[] inv = getPoly().inverse(p, 8);
        PrimitiveBuffers.release(inv);
    }

    @Test
    public void convolution() {
        int[] a = new int[]{0, 1};
        int[] b = new int[]{2, 3};
        int[] ans = new int[]{0, 2, 3, 0};

        int[] cv = getPoly().convolution(a, b);
        Assert.assertTrue(equal(ans, cv));

        PrimitiveBuffers.release(cv);
    }

    @Test
    public void multiApply() {
        int[] p = new int[]{0, 1, 2, 0, 0, 0, 0, 1};
        int[] x = new int[]{1, 2, 3, 4, 5, 6, 7, 8};
        int[] y = new int[8];

        getPoly().multiApply(p, x, y, 8);
        for (int i = 0; i < 8; i++) {
            Assert.assertEquals(getPoly().apply(p, x[i]), y[i]);
        }
    }

    @Test
    public void pow() {
        int[] p = new int[]{1, 1};
        int[] ans = getPoly().modpow(p, 4, 5);
        int[] ans2 = getPoly().modpowByLnExp(p, 4, 5);
        Assert.assertTrue(equal(ans, ans2));
        PrimitiveBuffers.release(ans, ans2);
    }

    @Test
    public void deltaConvolution() {
        int[] a = new int[]{1, 2, 3, 0};
        int[] b = new int[]{3, 2, 1, 0};
        int[] ans = new int[]{1 * 3 + 2 * 2 + 3 * 1, 2 * 3 + 3 * 2, 3 * 3};

        int[] dc = getPoly().deltaConvolution(a, b);
        Assert.assertTrue(equal(ans, dc));
        PrimitiveBuffers.release(dc);
    }
}
